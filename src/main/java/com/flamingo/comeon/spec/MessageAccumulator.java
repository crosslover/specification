package com.flamingo.comeon.spec;

import java.util.List;

/**
 * Extension for MessageSpecification, which is intended for accumulate the messages when
 * {@link MessageSpecification}s are composite.
 *
 * @param <M>
 * @param <T>
 * @author wyh
 */
public abstract class MessageAccumulator<M, T> extends MessageSpecification<M, T> {
    @Override
    public abstract MessageAccumulator<M, T> not();

    public abstract MessageAccumulator<M, T> and(MessageAccumulator<M, T> other);

    public abstract MessageAccumulator<M, T> or(MessageAccumulator<M, T> other);

    public abstract List<M> getMessages();

    public abstract M getMessage(MessageReducer<M> reducer);

    protected abstract List<MessageSpecification<M, T>> getSpecifications();

}
