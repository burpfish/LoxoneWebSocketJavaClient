package org.chelmer;

import org.chelmer.clientimpl.LoxoneWebSocketClient;

/**
 * Created by burfo on 09/03/2017.
 */
public class LoxoneClientFactory {
    public LoxoneClient createAndConnect(String host, int port, String user, String password, boolean useMockData) {
        LoxoneClient client = new LoxoneWebSocketClient(host, port, user, password);
        client.connectAndRegisterForUpdates(useMockData);
        return client;
    }

    public LoxoneClient create(String host, int port, String user, String password) {
        return new LoxoneWebSocketClient(host, port, user, password);
    }

    public LoxoneClient createAndConnect(String host, int port, String user, String password) {
        return createAndConnect(host, port, user, password, false);
    }
}
