package com.example.communication.model;

/**
 * Created by Thomas on 27.12.2016.
 */
public enum Frequency {
    daily, weekly, monthly, yearly, none;

    public static Frequency indexToFrequency(int idx)
    {
        Frequency frequency = null;
        if (idx == 0)
        {
            frequency = Frequency.daily;
        } else if (idx == 1)
        {
            frequency = Frequency.weekly;
        } else if (idx == 2)
        {
            frequency = Frequency.monthly;
        }
        else if (idx == 3)
        {
            frequency = Frequency.yearly;
        }
        else if (idx == 4)
        {
            frequency = Frequency.none;
        }

        return frequency;
    }

    public static int FrequencyToIndex(Frequency frequency)
    {
        switch (frequency)
        {
            case daily:
                return 0;
            case weekly:
                return 1;
            case monthly:
                return 2;
            case yearly:
                return 3;
            case none:
                return 4;
        }

        return -1;
    }


}
