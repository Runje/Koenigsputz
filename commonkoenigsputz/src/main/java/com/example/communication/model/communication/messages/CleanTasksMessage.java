package com.example.communication.model.communication.messages;

import com.example.communication.model.CleanTask;
import com.example.communication.model.communication.MessageConverter;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 11.01.2017.
 */

public class CleanTasksMessage extends KoenigsputzMessage {

    public static final String NAME = "CleanTasksMessage";
    List<CleanTask> cleanTasks;

    public CleanTasksMessage(List<CleanTask> cleanTasks) {
        this.cleanTasks = cleanTasks;
    }

    public CleanTasksMessage(String fromId, String toId, DateTime timestamp, ByteBuffer buffer) {
        this.fromId = fromId;
        this.toId = toId;
        int size = buffer.getInt();
        cleanTasks = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            cleanTasks.add(MessageConverter.byteToCleanTask(buffer));
        }
    }
    public String getName() {
        return NAME;
    }

    @Override
    protected int getContentLength() {
        int size = 4;
        for (int i = 0; i < cleanTasks.size(); i++) {
            CleanTask cleanTask = cleanTasks.get(i);
            size += MessageConverter.getLength(cleanTask);
        }

        return size;
    }

    @Override
    protected byte[] contentToByte() {
        int size = 4;
        byte[][] bytes = new byte[cleanTasks.size()][];
        for (int i = 0; i < cleanTasks.size(); i++) {
            CleanTask cleanTask = cleanTasks.get(i);
            bytes[i] = MessageConverter.cleanTaskToBytes(cleanTask);
            size += bytes[i].length;
        }
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.putInt(cleanTasks.size());
        for (int i = 0; i < bytes.length; i++) {
            byte[] aByte = bytes[i];
            buffer.put(aByte);
        }

        return buffer.array();
    }

    public List<CleanTask> getCleanTasks() {
        return cleanTasks;
    }

    @Override
    public String toString() {
        return "CleanTasksMessage{" +
                "cleanTasks=" + cleanTasks +
                '}';
    }
}
