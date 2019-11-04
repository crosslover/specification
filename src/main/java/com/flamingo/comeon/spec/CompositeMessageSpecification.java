package com.flamingo.comeon.spec;

import com.flamingo.comeon.spec.util.CollectionUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link MessageSpecification}'s composite implementation which the messages are accumulated eagerly recursively.
 * Better to make it lazily maybe.
 *
 * @author wyh
 */
public class CompositeMessageSpecification<M, T> extends MessageAccumulator<M, T> {
    private CompositeMessageSpecificationDelegate<M, T> delegate;

    public CompositeMessageSpecification(List<MessageSpecification<M, T>> leafSpecifications) {
        delegate = new CompositeMessageSpecificationDelegate<>(leafSpecifications);
    }

    public CompositeMessageSpecification(List<MessageSpecification<M, T>> leafSpecifications, Operator operator) {
        delegate = new CompositeMessageSpecificationDelegate<>(leafSpecifications, operator);
    }

    @Override
    public boolean isSatisfiedBy(T target) {
        return delegate.isSatisfiedBy(target);
    }

    @Override
    public MessageAccumulator<M, T> and(MessageSpecification<M, T> other) {
        return delegate.and(other);
    }

    @Override
    public MessageAccumulator<M, T> or(MessageSpecification<M, T> other) {
        return delegate.or(other);
    }

    @Override
    public MessageAccumulator<M, T> not() {
        return delegate.not();
    }

    @Override
    public MessageAccumulator<M, T> and(MessageAccumulator<M, T> other) {
        return delegate.and(other);
    }

    @Override
    public MessageAccumulator<M, T> or(MessageAccumulator<M, T> other) {
        return delegate.or(other);
    }

    @Override
    public List<M> getMessages() {
        return delegate.result ? delegate.successList : delegate.failList;
    }

    @Override
    public M getMessage(MessageReducer<M> reducer) {
        return reducer.reduce(getMessages());
    }

    @Override
    public M getMessage() {
        return (M) getMessage(MessageReducer.LAST);
    }

    @Override
    protected List<MessageSpecification<M, T>> getSpecifications() {
        return delegate.leafSpecifications;
    }

    private class CompositeMessageSpecificationDelegate<M, T> extends CompositeSpecification<MessageSpecification<M, T>, T> {

        List<M> successList = CollectionUtil.newArrayList();
        List<M> failList = CollectionUtil.newArrayList();
        boolean result;

        public CompositeMessageSpecificationDelegate(List<MessageSpecification<M, T>> leafSpecifications) {
            super(leafSpecifications);
        }

        public CompositeMessageSpecificationDelegate(List<MessageSpecification<M, T>> leafSpecifications, Operator operator) {
            super(leafSpecifications, operator);
        }

        @Override
        public boolean isSatisfiedBy(T target) {
            return result = super.isSatisfiedBy(target);
        }

        @Override
        public MessageAccumulator<M, T> not() {
            return new CompositeMessageSpecification<>(
                    leafSpecifications.stream().map(MessageSpecification::not).collect(Collectors.toList()),
                    operator == Operator.AND ? Operator.OR : Operator.AND);
        }

        @Override
        protected void postLeafExecutedHook(boolean result, MessageSpecification<M, T> specification) {
            if (specification instanceof MessageAccumulator
                    && !CollectionUtil.isEmpty(((MessageAccumulator<M, T>) specification).getMessages())) {
                (result ? successList : failList)
                        .addAll(((MessageAccumulator<M, T>) specification).getMessages());
            } else {
                if (specification.getMessage() != null) {
                    (result ? successList : failList).add(specification.getMessage());
                }
            }
        }

        public MessageAccumulator<M, T> and(MessageSpecification<M, T> other) {
            if (operator == Operator.AND) {
                super.and(other);
                return (MessageAccumulator<M, T>) CompositeMessageSpecification.this;
            }
            return new CompositeMessageSpecification<>(
                    CollectionUtil.newArrayList((MessageSpecification<M, T>) CompositeMessageSpecification.this,
                            other), Operator.AND);
        }

        public MessageAccumulator<M, T> or(MessageSpecification<M, T> other) {
            if (operator == Operator.OR) {
                super.and(other);
                return (MessageAccumulator<M, T>) CompositeMessageSpecification.this;
            }
            return new CompositeMessageSpecification<>(
                    CollectionUtil.newArrayList((MessageSpecification<M, T>) CompositeMessageSpecification.this,
                            other), Operator.OR);
        }

        public MessageAccumulator<M, T> and(MessageAccumulator<M, T> other) {
            if (operator == Operator.AND) {
                leafSpecifications.addAll(other.getSpecifications());
                return (MessageAccumulator<M, T>) CompositeMessageSpecification.this;
            }
            return new CompositeMessageSpecification<>(
                    CollectionUtil.newArrayList((MessageSpecification<M, T>) CompositeMessageSpecification.this,
                            other), Operator.AND);
        }

        public MessageAccumulator<M, T> or(MessageAccumulator<M, T> other) {
            if (operator == Operator.OR) {
                leafSpecifications.addAll(other.getSpecifications());
                return (MessageAccumulator<M, T>) CompositeMessageSpecification.this;
            }
            return new CompositeMessageSpecification<>(
                    CollectionUtil.newArrayList((MessageSpecification<M, T>) CompositeMessageSpecification.this,
                            other), Operator.OR);
        }
    }
}
