package com.example.communication.model.communication.messages;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 22.01.2017.
 */
public abstract class KoenigsputzAnsMessage extends KoenigsputzMessage {
    protected DateTime syncTimestamp;


    public DateTime getSyncTimestamp() {
        return syncTimestamp;
    }

    public int getTotalLength()
    {
        return super.getTotalLength() + 8;
    }

    protected void headerToBuffer(ByteBuffer buffer) {
        super.headerToBuffer(buffer);
        buffer.putLong(syncTimestamp.getMillis());
    }
}
