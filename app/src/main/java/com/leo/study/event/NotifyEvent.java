package com.leo.study.event;

/**
 * Created by binhbt on 2/28/2017.
 */
public class NotifyEvent {
    private Object payload;
    private Type type;

    public NotifyEvent(Object payload, Type type) {
        this.payload = payload;
        this.type = type;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type{
        TOPIC_ADDED,
        TOPIC_DELETE,
        WORD_ADDED,
        WORD_DELETE,
        TOPIC_SELECTED,
        TOPIC_REMOVED,
        WORD_TEST_SELECTED
    }
}
