package com.flamingo.comeon.spec;

import com.flamingo.comeon.spec.util.CollectionUtil;

import java.util.List;

public interface MessageReducer<M> {
    M reduce(List<M> messages);

    MessageReducer FIRST = messages -> CollectionUtil.isEmpty(messages) ? null : messages.get(0);

    MessageReducer LAST = messages -> CollectionUtil.isEmpty(messages) ? null : messages.get(messages.size() - 1);
}
