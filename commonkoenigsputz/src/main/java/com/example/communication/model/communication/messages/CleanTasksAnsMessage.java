package com.example.communication.model.communication.messages;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.01.2017.
 */

public class CleanTasksAnsMessage extends KoenigsputzAnsMessage {

    public static final String NAME = "CleanTasksAnsMessage";

    private int[] oldIds;
    private int[] newIds;

    public CleanTasksAnsMessage(int[] oldIds, int[] newIds, DateTime syncTimestamp) {
        this.oldIds = oldIds;
        this.newIds = newIds;
        this.syncTimestamp = syncTimestamp;
    }

    public CleanTasksAnsMessage(String fromId, String toId, DateTime syncTimestamp, ByteBuffer buffer) {
        this.fromId = fromId;
        this.toId = toId;
        this.syncTimestamp = syncTimestamp;
        int count = buffer.getInt();
        oldIds = new int[count];
        newIds = new int[count];
        for (int i = 0; i < count; i++) {
           oldIds[i] = buffer.getInt();
        }

        for (int i = 0; i < count; i++) {
            newIds[i] = buffer.getInt();
        }
    }


    public String getName() {
        return NAME;
    }

    public int[] getOldIds() {
        return oldIds;
    }

    public int[] getNewIds() {
        return newIds;
    }


    @Override
    protected int getContentLength() {
        return 4 + oldIds.length * 4 * 2;
    }

    @Override
    protected byte[] contentToByte() {

        ByteBuffer buffer = ByteBuffer.allocate(getContentLength());
        buffer.putInt(oldIds.length);
        for (int i = 0; i < oldIds.length; i++) {
            buffer.putInt(oldIds[i]);
        }

        for (int i = 0; i < newIds.length; i++) {
            buffer.putInt(newIds[i]);
        }

        return buffer.array();
    }

}
