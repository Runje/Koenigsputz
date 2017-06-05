package com.apps.koenig.thomas.koenigsputz.AppModel.communication;

import com.example.OnConnectionChangedListener;
import com.example.OnReceiveBytesListener;
import com.example.SocketChannelTCPClient;
import com.example.communication.model.communication.ConnectUtils;
import com.example.communication.model.communication.Parser;
import com.example.communication.model.communication.messages.KoenigsputzMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 08.01.2017.
 */
public class ServerConnection extends SocketChannelTCPClient implements Connection, OnConnectionChangedListener, OnReceiveBytesListener {
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private ConnectionEventListener connectionEventListener;
    private String fromId;


    public ServerConnection(String fromId) {
        super(ConnectUtils.PORT, ConnectUtils.SERVER_IP);
        this.fromId = fromId;
        addOnConnectionChangedListener(this);
        addOnReceiveBytesListener(this);
    }


    @Override
    public boolean isConnected() {
        return super.isConnected();
    }

    @Override
    public void connect() {
        logger.info("Trying to connect...");
        tryConnect();
    }

    @Override
    public void disconnect() {
        super.disconnect();
    }

    @Override
    public void setOnConnectionEventListener(ConnectionEventListener connectionEventListener) {
        this.connectionEventListener = connectionEventListener;
    }

    @Override
    public void sendKoenigsputzMessage(KoenigsputzMessage msg) {
        msg.setFromId(fromId);
        msg.setToId(KoenigsputzMessage.ServerId);
        super.sendMessage(msg);
    }

    @Override
    public void onConnectionChanged(boolean b) {
        if (connectionEventListener != null)
        {
            connectionEventListener.onConnectionStatusChange(b);
        }
    }

    @Override
    public void onReceiveBytes(byte[] bytes) {

        logger.info("Receive bytes: " + bytes.length);
        KoenigsputzMessage msg = Parser.parse(ByteBuffer.wrap(bytes));

        logger.info(msg.toString());
        if (connectionEventListener != null)
        {
            connectionEventListener.onReceiveMessage(msg);
        }
    }
}
