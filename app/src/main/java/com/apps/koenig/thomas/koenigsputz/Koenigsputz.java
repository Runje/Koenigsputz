package com.apps.koenig.thomas.koenigsputz;

import android.content.Context;
import android.content.SharedPreferences;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Thomas on 10.11.2016.
 */

public class Koenigsputz {

    public static final String SHARED_PREF = "KOENIGSPUTZ_SHARED_PREF";
    public static final String NAME = "NAME";
    public static final String NO_NAME = "NO_NAME";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("dd.MM.YY");
    private static final String LAST_SYNC_DATE = "LASTSYNCDATE";
    private static final long NO_DATE_LONG = 0;
    private static final DateTime NO_DATE = new DateTime(NO_DATE_LONG);

    private static String name = "";

    /**
     * Gets the name of the User.
     * @return Name of the User
     */
    public static String getName(Context context) {
        if (name.equals(""))
        {
            SharedPreferences preferences = context.getSharedPreferences(Koenigsputz.SHARED_PREF, MODE_PRIVATE);
            name = preferences.getString(Koenigsputz.NAME, Koenigsputz.NO_NAME);
        }

        return name;
    }

    /**
     * Saves the name to the shared preferences
     * @param name
     * @param context
     */
    public static void saveName(String name, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Koenigsputz.SHARED_PREF, MODE_PRIVATE);
        preferences.edit().putString(NAME, name).commit();
    }

    /**
     * Gets the last sync date from shared prefs.
     * @param context
     * @return
     */
    public static DateTime getLastSyncDate(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Koenigsputz.SHARED_PREF, MODE_PRIVATE);
        return new DateTime(preferences.getLong(Koenigsputz.LAST_SYNC_DATE, NO_DATE_LONG));
    }

    /**
     * Saves the last sync date to shared prefs.
     * @param date
     * @param context
     */
    public static void saveLastSyncDate(DateTime date, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Koenigsputz.SHARED_PREF, MODE_PRIVATE);
        preferences.edit().putLong(LAST_SYNC_DATE, date.getMillis()).commit();
    }
}
