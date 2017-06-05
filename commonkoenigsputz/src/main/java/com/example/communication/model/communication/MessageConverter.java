package com.example.communication.model.communication;

import com.example.communication.model.CleanTask;
import com.example.communication.model.Frequency;
import com.example.communication.model.database.DatabaseItem;

import org.joda.time.DateTime;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 14.02.2015.
 */
public class MessageConverter
{
    public static final int sizeLength = 4;
    public static final String encoding = "ISO-8859-1";
    public static final int idLength = 36;
    private static int DatabaseItemLength = 4 + 4 + 72 + 16 + 1;
    public static int stringLengthSize = 2;

    public static byte[] stringToByte(String string)
    {
        try
        {
            ByteBuffer buffer = ByteBuffer.allocate(2 + string.length());
            if (string.length() > Short.MAX_VALUE)
            {
                throw new RuntimeException("String longer than Short.Max_value");
            }
            buffer.putShort((short) string.length());
            buffer.put(string.getBytes(encoding));
            return buffer.array();
        } catch (UnsupportedEncodingException e)
        {

            e.printStackTrace();
            return null;
        }
    }

    public static String byteToString(ByteBuffer buffer)
    {
        short length = buffer.getShort();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes, Charset.forName(encoding)).trim();
    }



    public static byte booleanToByte(boolean b)
    {
        return (byte) (b ? 1 : 0);
    }

    public static boolean byteToBoolean(Byte b)
    {
        return b == (byte) 1;
    }

    public static byte[] cleanTaskToBytes(CleanTask cleanTask)
    {
        String frequency = cleanTask.getFrequency().toString();
        String description = cleanTask.getDescription();
        String name = cleanTask.getName();
        String responsible = cleanTask.getResponsible();
        ByteBuffer buffer = ByteBuffer.allocate(getLength(cleanTask));
        buffer.put(databaseItemToBytes(cleanTask));
        buffer.put(stringToByte(name));
        buffer.put(stringToByte(description));
        buffer.put(stringToByte(responsible));
        buffer.putInt(cleanTask.getDurationInMin());
        buffer.putInt(cleanTask.getDifficulty());
        buffer.put(stringToByte(frequency));
        buffer.putInt(cleanTask.getFrequencyNumber());
        buffer.putLong(cleanTask.getFirstDate().getMillis());
        return buffer.array();
    }

    public static CleanTask byteToCleanTask(ByteBuffer buffer) {
        int id = buffer.getInt();
        String createdFrom = byteToString(buffer);
        String lastModifiedFrom = byteToString(buffer);
        DateTime insertDate = new DateTime(buffer.getLong());
        DateTime lastModifiedDate = new DateTime(buffer.getLong());
        boolean deleted = byteToBoolean(buffer.get());
        String name = byteToString(buffer);
        String description = byteToString(buffer);
        String responsible = byteToString(buffer);
        int duration = buffer.getInt();
        int difficulty = buffer.getInt();
        Frequency frequency = Frequency.valueOf(byteToString(buffer));
        int frequencyNumber = buffer.getInt();
        DateTime firstDate = new DateTime(buffer.getLong());
        return new CleanTask(name, description, responsible, difficulty, duration, firstDate, frequency, frequencyNumber, id, insertDate, lastModifiedDate, deleted, createdFrom, lastModifiedFrom);
    }

    public static byte[] databaseItemToBytes(DatabaseItem item) {
        ByteBuffer buffer = ByteBuffer.allocate(DatabaseItemLength);
        buffer.putInt(item.getId());
        buffer.put(stringToByte(item.getCreatedFrom()));
        buffer.put(stringToByte(item.getLastChangeFrom()));
        buffer.putLong(item.getInsertDate().getMillis());
        buffer.putLong(item.getLastModifiedDate().getMillis());
        booleanToByte(item.isDeleted());
        return buffer.array();
    }

    public static int getLength(CleanTask cleanTask) {
        String frequency = cleanTask.getFrequency().toString();
        String description = cleanTask.getDescription();
        String name = cleanTask.getName();
        String responsible = cleanTask.getResponsible();
        return DatabaseItemLength + 2 + name.length() + 2 + description.length() + 2 + responsible.length() + 8 + 2 + frequency.length() + 12;
    }
}
