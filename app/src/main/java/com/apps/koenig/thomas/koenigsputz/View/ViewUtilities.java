package com.apps.koenig.thomas.koenigsputz.View;

import android.widget.DatePicker;

import org.joda.time.DateTime;

/**
 * Created by Thomas on 27.12.2016.
 */
public class ViewUtilities {
    public static DateTime getDateFromDatePicker(DatePicker datePicker)
    {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        return new DateTime(year, month, day, 0, 0, 0);
    }

    public static void setDateToDatePicker(DatePicker datePicker, DateTime time)
    {
        int year = time.getYear();
        int month = time.getMonthOfYear() - 1;      // Need to subtract 1 here.
        int day = time.getDayOfMonth();

        datePicker.updateDate(year, month, day);
    }
}
