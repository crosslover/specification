package com.flamingo.comeon.spec;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link MessageSpecification}'s composite implementation.
 *
 * @author wyh
 */
public class CompositeMessageSpecification<M, MS extends MessageSpecification<M, T>, T> extends MessageSpecification<M, T> {
    private CompositeMessageSpecificationDelegate<M, MS, T> delegate;

    public CompositeMessageSpecification(List<MS> leafSpecifications) {
        delegate = new CompositeMessageSpecificationDelegate<>(leafSpecifications);
    }

    public CompositeMessageSpecification(List<MS> leafSpecifications, CompositeSpecification.Operator operator) {
        delegate = new CompositeMessageSpecificationDelegate<>(leafSpecifications, operator);
    }

    @Override
    public M getMessage() {
        return delegate.message;
    }

    @Override
    public MessageSpecification<M, T> and(MessageSpecification<M, T> other) {
        return (MessageSpecification<M, T>) delegate.and(other);
    }

    @Override
    public MessageSpecification<M, T> or(MessageSpecification<M, T> other) {
        return (MessageSpecification<M, T>) delegate.or(other);
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
        private M message;

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
        protected boolean beforeShortCircuit(MS leaf) {
            message = leaf.getMessage();
            return true;
        }
    }
}
