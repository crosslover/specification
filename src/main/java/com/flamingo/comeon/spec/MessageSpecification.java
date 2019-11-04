package com.flamingo.comeon.spec;

/**
 * {@link Specification<T>} that could hold messages.
 *
 * @author wyh
 */
public abstract class MessageSpecification<M, T> extends AbstractSpecification<T>{
    public abstract M getMessage();

    public abstract MessageAccumulator<M, T> and(MessageSpecification<M, T> other);

    public abstract MessageAccumulator<M, T> or(MessageSpecification<M, T> other);

    @Override
    public abstract MessageSpecification<M, T> not();

}
