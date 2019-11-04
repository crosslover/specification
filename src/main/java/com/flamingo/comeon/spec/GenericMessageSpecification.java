package com.flamingo.comeon.spec;

import com.flamingo.comeon.spec.util.CollectionUtil;

/**
 * Generic implementation for {@link MessageSpecification}.
 * Note that the basic usage for {@code getMessage},
 * so it is best to make this to be a value object.
 *
 * @author wyh
 */
public class GenericMessageSpecification<T, M> extends MessageSpecification<M, T> {

    private Specification<T> specification;
    private M success;
    private M fail;
    private boolean result;

    public GenericMessageSpecification(Specification<T> specification, M success, M fail) {
        this.specification = specification;
        this.success = success;
        this.fail = fail;
    }

    /**
     * get the message.
     *
     * @return {@code success} when {@code isSatisfiedBy} is true,
     * {@code fail} when {@code isSatisfiedBy} is false.
     */
    @Override
    public M getMessage() {
        return result ? success : fail;
    }

    @Override
    public boolean isSatisfiedBy(T target) {
        return result = specification.isSatisfiedBy(target);
    }

    @Override
    public MessageAccumulator<M, T> and(MessageSpecification<M, T> other) {
        return new CompositeMessageSpecification(CollectionUtil.newArrayList(this, other));
    }

    @Override
    public MessageAccumulator<M, T> or(MessageSpecification<M, T> other) {
        return new CompositeMessageSpecification(CollectionUtil.newArrayList(this, other), Operator.OR);
    }

    @Override
    public MessageSpecification<M, T> not() {
        return new GenericMessageSpecification<>(specification.not(), fail, success);
    }
}
