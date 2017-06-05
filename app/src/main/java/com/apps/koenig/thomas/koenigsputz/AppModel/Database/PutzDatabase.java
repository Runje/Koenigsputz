package com.apps.koenig.thomas.koenigsputz.AppModel.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.communication.model.CleanTask;
import com.example.communication.model.database.BaseColumns;
import com.example.communication.model.database.CleanTaskEntry;
import com.example.communication.model.database.Database;
import com.example.communication.model.Frequency;
import com.example.communication.model.database.DatabaseItem;
import com.example.communication.model.database.DatabaseItemEntry;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 06.01.2017.
 */
public class PutzDatabase extends SQLiteOpenHelper implements Database {
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Putzkoenig.db";

    public PutzDatabase(Context context, String name)    {
        super(context, name, null, DATABASE_VERSION);

    }

    public PutzDatabase(Context context)    {
        this(context, DATABASE_NAME);
    }

    public PutzDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        logger.info("Creating db, version: " + DATABASE_VERSION);
        db.execSQL(CleanTaskEntry.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        logger.info("Updating db, version: " + oldVersion + " -> " + newVersion);
        // TODO:
    }

    /***
     * Methods from interface Database
     */

    /**
     * Adding clean task to the database
     * @param cleanTask
     */
    @Override
    public void addCleanTask(CleanTask cleanTask) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(CleanTaskEntry.TABLE, null, cleanTaskToValues(cleanTask));
        db.close();
    }

    @Override
    public List<CleanTask> getAllCleanTasks() {
        SQLiteDatabase db = getReadableDatabase();
        List<CleanTask> cleanTasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CleanTaskEntry.TABLE + " WHERE " + DatabaseItemEntry.COLUMN_DELETED + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{"0"});

        if (cursor.moveToFirst())
        {
            do
            {
                CleanTask cleanTask = createCleanTaskFromCursor(cursor);
                cleanTasks.add(cleanTask);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return cleanTasks;
    }

    @Override
    public void changeCleanTaskId(int oldId, int newId) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CleanTaskEntry._ID, newId);

        db.update(CleanTaskEntry.TABLE, values, CleanTaskEntry._ID + " = ?", new String[] {Integer.toString(oldId)} );
    }

    @Override
    public boolean cleanTaskIdExists(int id) {
        String selectQuery = "SELECT 1 FROM " + CleanTaskEntry.TABLE + " WHERE " + CleanTaskEntry._ID + " = ?";

        Cursor cursor = getReadableDatabase().rawQuery(selectQuery, new String[]{Integer.toString(id)});

        boolean result = false;
        if (cursor.moveToFirst())
        {
            result = cursor.getCount() > 0;
        }

        cursor.close();
        return result;
    }

    @Override
    public CleanTask getCleanTaskWithId(int id) {
        String selectQuery = "SELECT 1 FROM " + CleanTaskEntry.TABLE + " WHERE " + CleanTaskEntry._ID + " = ?";

        Cursor cursor = getReadableDatabase().rawQuery(selectQuery, new String[]{Integer.toString(id)});

        CleanTask result = null;
        if (cursor.moveToFirst())
        {
            result = createCleanTaskFromCursor(cursor);
        }

        cursor.close();
        return result;
    }

    @Override
    public void overwrite(CleanTask cleanTask) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = cleanTaskToValues(cleanTask);
        db.update(CleanTaskEntry.TABLE, values, CleanTaskEntry._ID + " = " + cleanTask.getId(), null);
    }

    @Override
    public List<CleanTask> getChangesSince(DateTime dateTime, String userId) {
        ArrayList<CleanTask> cleanTasks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + CleanTaskEntry.TABLE + " WHERE " + DatabaseItemEntry.COLUMN_DELETED + " = ? AND " + DatabaseItemEntry.COLUMN_MODIFIED_DATE + " > ? AND " + DatabaseItemEntry.COLUMN_MODIFIED_ID + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{"0", Long.toString(dateTime.getMillis()), userId});

        if (cursor.moveToFirst())
        {
            do
            {
                CleanTask cleanTask = createCleanTaskFromCursor(cursor);
                cleanTasks.add(cleanTask);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return cleanTasks;
    }

    @Override
    public List<CleanTask> getComingCleanTasks(int days) {
        ArrayList<CleanTask> cleanTasks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + CleanTaskEntry.TABLE + " WHERE " + DatabaseItemEntry.COLUMN_DELETED + " = ? AND " + CleanTaskEntry.COLUMN_FIRST_DATE + " > ? AND "+ CleanTaskEntry.COLUMN_FIRST_DATE + " < ? AND " + CleanTaskEntry.COLUMN_FREQUENCY + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{"0", Long.toString(DateTime.now().withTimeAtStartOfDay().getMillis()), Long.toString(DateTime.now().plusDays(days).getMillis()), Frequency.none.toString()});

        if (cursor.moveToFirst())
        {
            do
            {
                CleanTask cleanTask = createCleanTaskFromCursor(cursor);
                cleanTasks.add(cleanTask);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return cleanTasks;
    }

    @Override
    public List<CleanTask> getAllFrequentTasks() {
        ArrayList<CleanTask> cleanTasks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + CleanTaskEntry.TABLE + " WHERE " + DatabaseItemEntry.COLUMN_DELETED + " = ? AND " + CleanTaskEntry.COLUMN_FREQUENCY + " != ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{"0", Frequency.none.toString()});

        if (cursor.moveToFirst())
        {
            do
            {
                CleanTask cleanTask = createCleanTaskFromCursor(cursor);
                cleanTasks.add(cleanTask);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return cleanTasks;
    }


    /***
     * Helper methods
     */

    private static CleanTask createCleanTaskFromCursor(Cursor cursor) {
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        String description = cursor.getString(2);
        String responsible = cursor.getString(3);
        int difficulty = cursor.getInt(4);
        int duration = cursor.getInt(5);
        DateTime firstDate = new DateTime(cursor.getLong(6));
        String frequency = cursor.getString(7);
        int frequencyNumber = cursor.getInt(8);
        boolean deleted = cursor.getInt(9) != 0;
        DateTime insertDate = new DateTime(cursor.getLong(10));
        String insertId = cursor.getString(11);
        DateTime modifiedDate = new DateTime(cursor.getLong(12));
        String modifiedId = cursor.getString(13);
        return new CleanTask(name, description,responsible, difficulty, duration, firstDate, frequency == null ? null : Frequency.valueOf(frequency), frequencyNumber, id, insertDate, modifiedDate, deleted, insertId, modifiedId);
    }

    private static ContentValues cleanTaskToValues(CleanTask cleanTask) {
        ContentValues values = new ContentValues();
        if (cleanTask.getId() > 0)
        {
            values.put(CleanTaskEntry._ID, cleanTask.getId());
        }

        values.put(CleanTaskEntry.COLUMN_NAME, cleanTask.getName().trim());
        values.put(CleanTaskEntry.COLUMN_DESCRIPTION, cleanTask.getDescription().trim());
        values.put(CleanTaskEntry.COLUMN_RESPONSIBLE, cleanTask.getResponsible().trim());
        values.put(CleanTaskEntry.COLUMN_DIFFICULTY, cleanTask.getDifficulty());
        values.put(CleanTaskEntry.COLUMN_DURATION, cleanTask.getDurationInMin());
        values.put(CleanTaskEntry.COLUMN_FIRST_DATE, cleanTask.getFirstDate().getMillis());
        Frequency frequency = cleanTask.getFrequency();
        values.put(CleanTaskEntry.COLUMN_FREQUENCY, frequency == null ? null : frequency.toString());
        values.put(CleanTaskEntry.COLUMN_FREQUENCY_NUMBER, cleanTask.getFrequencyNumber());
        values.put(DatabaseItemEntry.COLUMN_DELETED, cleanTask.isDeleted());
        values.put(DatabaseItemEntry.COLUMN_INSERT_DATE, cleanTask.getInsertDate().getMillis());
        values.put(DatabaseItemEntry.COLUMN_INSERT_ID, cleanTask.getCreatedFrom());
        values.put(DatabaseItemEntry.COLUMN_MODIFIED_DATE, cleanTask.getLastModifiedDate().getMillis());
        values.put(DatabaseItemEntry.COLUMN_MODIFIED_ID, cleanTask.getLastChangeFrom());
        return values;
    }

    public boolean doesExistWithId(int id) {
        String selectQuery = "SELECT * FROM " + CleanTaskEntry.TABLE + " WHERE " + CleanTaskEntry._ID + " = ?";

        Cursor cursor = getReadableDatabase().rawQuery(selectQuery, new String[]{Integer.toString(id)});

        boolean result = false;
        if (cursor.moveToFirst())
        {
            result = cursor.getCount() > 0;
        }

        cursor.close();
        return result;
    }

    public void resetAll() {
        getWritableDatabase().execSQL(CleanTaskEntry.DELETE);
        getWritableDatabase().execSQL(CleanTaskEntry.CREATE);
    }
}
