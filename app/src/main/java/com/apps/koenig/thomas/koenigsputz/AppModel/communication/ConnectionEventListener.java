package com.apps.koenig.thomas.koenigsputz.AppModel.communication;

import com.example.communication.model.communication.messages.KoenigsputzMessage;

/**
 * Created by Thomas on 12.01.2017.
 */
public interface ConnectionEventListener {
    void onConnectionStatusChange(boolean connected);
    void onReceiveMessage(KoenigsputzMessage message);
}
