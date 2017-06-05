package com.example.communication.model.database;


/**
 * Created by Thomas on 08.01.2017.
 */

public class CleanTaskEntry implements BaseColumns {
    public static final String TABLE = "clean_task";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_RESPONSIBLE = "responsible";
    public static final String COLUMN_DIFFICULTY = "difficulty";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_FIRST_DATE = "first_date";
    public static final String COLUMN_FREQUENCY = "frequency";
    public static final String COLUMN_FREQUENCY_NUMBER = "frequency_number";


    public static final String CREATE = "CREATE TABLE " + TABLE + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_RESPONSIBLE + " TEXT, " +
            COLUMN_DIFFICULTY + " INT, " +
            COLUMN_DURATION + " INT, " +
            COLUMN_FIRST_DATE + " LONG, " +
            COLUMN_FREQUENCY + " TEXT, " +
            COLUMN_FREQUENCY_NUMBER + " INT, " +
            DatabaseItemEntry.COLUMN_DELETED + " INT, " +
            DatabaseItemEntry.COLUMN_INSERT_DATE + " LONG, " +
            DatabaseItemEntry.COLUMN_INSERT_ID + " TEXT, " +
            DatabaseItemEntry.COLUMN_MODIFIED_DATE + " LONG, " +
            DatabaseItemEntry.COLUMN_MODIFIED_ID + " TEXT" +
            ");";

    public static final String DELETE = "DROP TABLE IF EXISTS " + TABLE;


}
