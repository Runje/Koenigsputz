package com.apps.koenig.thomas.koenigsputz.AppModel.communication;

import com.example.communication.model.communication.messages.KoenigsputzMessage;

/**
 * Created by Thomas on 08.01.2017.
 */

public interface Connection {
    boolean isConnected();
    void connect();
    void disconnect();
    void setOnConnectionEventListener(ConnectionEventListener connectionEventListener);

    void sendKoenigsputzMessage(KoenigsputzMessage message);
}
