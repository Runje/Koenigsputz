package com.example.communication.model.communication.messages;

import com.example.communication.model.communication.MessageConverter;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.01.2017.
 */

public class AskForUpdatesMessage extends KoenigsputzMessage {

    public static final String NAME = "AskForUpdatesMessage";
    String username;
    DateTime lastSyncDate;

    public AskForUpdatesMessage(String name, DateTime lastSyncDate) {
        this.username = name;
        this.lastSyncDate = lastSyncDate;
    }

    public AskForUpdatesMessage(String fromId, String toId, DateTime timestamp, ByteBuffer buffer) {
        this.fromId = fromId;
        this.toId = toId;
        this.username = MessageConverter.byteToString(buffer);
        this.lastSyncDate = new DateTime(buffer.getLong());
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return NAME;
    }



    @Override
    protected int getContentLength() {
        return username.length() + MessageConverter.stringLengthSize + 8;
    }

    @Override
    protected byte[] contentToByte() {

        ByteBuffer buffer = ByteBuffer.allocate(getContentLength());
        buffer.put(MessageConverter.stringToByte(username));
        buffer.putLong(lastSyncDate.getMillis());
        return buffer.array();
    }

    @Override
    public String toString() {
        return "IdentifyMessage{" +
                "username='" + username + '\'' +
                '}';
    }
}
