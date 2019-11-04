package com.flamingo.comeon.spec.deprecated;

import com.flamingo.comeon.spec.CompositeSpecification;
import com.flamingo.comeon.spec.MessageAccumulator;
import com.flamingo.comeon.spec.MessageSpecification;
import com.flamingo.comeon.spec.Operator;
import com.flamingo.comeon.spec.Specification;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link MessageSpecification}'s composite implementation.
 *
 * @author wyh
 */
@Deprecated
public class CompositeMessageSpecification<M, MS extends MessageSpecification<M, T>, T> extends MessageSpecification<M, T> {
    private CompositeMessageSpecificationDelegate<M, MS, T> delegate;

    public CompositeMessageSpecification(List<MS> leafSpecifications) {
        delegate = new CompositeMessageSpecificationDelegate<>(leafSpecifications);
    }

    public CompositeMessageSpecification(List<MS> leafSpecifications, Operator operator) {
        delegate = new CompositeMessageSpecificationDelegate<>(leafSpecifications, operator);
    }

    @Override
    public M getMessage() {
        if (delegate.lastSatisfiedSpecification == null) {
            return null;
        }
        return delegate.lastSatisfiedSpecification.getMessage();
    }

    @Override
    public MessageAccumulator<M, T> and(MessageSpecification<M, T> other) {
        return (MessageAccumulator<M, T>) delegate.and(other);
    }

    @Override
    public MessageAccumulator<M, T> or(MessageSpecification<M, T> other) {
        return (MessageAccumulator<M, T>) delegate.or(other);
    }

    @Override
    public boolean isSatisfiedBy(T target) {
        return delegate.isSatisfiedBy(target);
    }

    @Override
    public MessageSpecification<M, T> not() {
        return (MessageSpecification<M, T>) delegate.not();
    }

    private static class CompositeMessageSpecificationDelegate<M, MS extends MessageSpecification<M, T>, T> extends CompositeSpecification<MS, T> {
        private MS lastSatisfiedSpecification;

        CompositeMessageSpecificationDelegate(List<MS> leafSpecifications) {
            super(leafSpecifications);
        }

        CompositeMessageSpecificationDelegate(List<MS> leafSpecifications, Operator operator) {
            super(leafSpecifications, operator);
        }

        @Override
        public Specification<T> not() {
            return new CompositeMessageSpecificationDelegate(
                    leafSpecifications.stream().map(Specification::not).collect(Collectors.toList()),
                    operator == Operator.AND ? Operator.OR : Operator.AND);
        }

        @Override
        protected void beforeShortCircuitHook(boolean result, MS leaf) {
            lastSatisfiedSpecification = leaf;
        }
    }
}
