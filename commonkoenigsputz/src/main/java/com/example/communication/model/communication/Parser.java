package com.example.communication.model.communication;

import com.example.communication.model.communication.messages.CleanTasksAnsMessage;
import com.example.communication.model.communication.messages.CleanTasksMessage;
import com.example.communication.model.communication.messages.AskForUpdatesMessage;
import com.example.communication.model.communication.messages.KoenigsputzMessage;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.01.2017.
 */

public class Parser {
    protected static Logger logger = LoggerFactory.getLogger("Parser");
    public static KoenigsputzMessage parse(ByteBuffer buffer) {
        String name = MessageConverter.byteToString(buffer);
        String fromId = MessageConverter.byteToString(buffer);
        String toId = MessageConverter.byteToString(buffer);
        DateTime timestamp = new DateTime(buffer.getLong());

        KoenigsputzMessage msg = null;
        switch(name)
        {
            case CleanTasksMessage.NAME:
                 msg = new CleanTasksMessage(fromId, toId, timestamp, buffer);
                break;

            case AskForUpdatesMessage.NAME:
                msg = new AskForUpdatesMessage(fromId, toId, timestamp, buffer);
                break;

            case CleanTasksAnsMessage.NAME:
                msg = new CleanTasksAnsMessage(fromId, toId, timestamp, buffer);
                break;
            default:
                logger.error("Unknown name: " + name);
                break;
        }

        return msg;
    }

}
