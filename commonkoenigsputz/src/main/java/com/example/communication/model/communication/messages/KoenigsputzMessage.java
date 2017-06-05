package com.example.communication.model.communication.messages;

import com.example.Message;
import com.example.communication.model.communication.MessageConverter;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.01.2017.
 */

public abstract class KoenigsputzMessage implements Message {
    protected Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    public static final String ServerId = "KOENIGSPUTZ_SERVER_ID";
    protected String fromId;
    protected String toId;

    public abstract String getName();

    public int getTotalLength()
    {
        int length = MessageConverter.sizeLength + 2 + getName().length() + 2 + fromId.length() + 2 + toId.length() + getContentLength();
        return length;
    }

    protected abstract int getContentLength();

    protected abstract byte[] contentToByte();

    public String getFromId()
    {
        return fromId;
    }

    public void setFromId(String fromId)
    {
        this.fromId = fromId;
    }

    public String getToId()
    {
        return toId;
    }

    public void setToId(String toId)
    {
        this.toId = toId;
    }

    protected void headerToBuffer(ByteBuffer buffer) {
        buffer.putInt(getTotalLength());
        buffer.put(MessageConverter.stringToByte(getName()));
        buffer.put(MessageConverter.stringToByte(fromId));
        buffer.put(MessageConverter.stringToByte(toId));
    }

    public ByteBuffer getBuffer()
    {
        ByteBuffer buffer = ByteBuffer.allocate(getTotalLength());
        buffer.putInt(getTotalLength());
        headerToBuffer(buffer);
        buffer.put(contentToByte());
        buffer.flip();
        return buffer;
    }
}
