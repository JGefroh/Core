package com.jgefroh.core;

/**
 * A marker interface for a message handler.
 * @author Joseph Gefroh
 */
public interface IMessageHandler<T extends IMessage> {
    
    /**
     * Perform this action when this message is received.
     * @param message   the message that was received
     */
    void onMessageReceived(final T message);
}
