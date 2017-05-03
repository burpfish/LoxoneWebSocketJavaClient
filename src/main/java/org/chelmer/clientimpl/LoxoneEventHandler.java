package org.chelmer.clientimpl;

/**
 * Created by burfo on 17/03/2017.
 */
public interface LoxoneEventHandler {
    void disconnect();

    void sendTextMessage(String s);

    void authComplete();
}
