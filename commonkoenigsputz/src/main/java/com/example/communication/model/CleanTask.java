package com.example.communication.model;

import com.example.communication.model.database.DatabaseItem;

import org.joda.time.DateTime;

/**
 * Created by Thomas on 12.11.2016.
 */

public class CleanTask extends DatabaseItem{

    public static int MinDifficulty = 0;
    public static int MaxDifficulty = 6;
    private String name;
    private String description;
    private String responsible;
    private int difficulty;
    private int durationInMin;
    private long firstDateInMs;
    private Frequency frequency;



    /**
     * Frequency multiplier.
     * 0 if not freqeuntly.
     */
    private int frequencyNumber;



    public CleanTask(String name, String description, String responsible, int difficulty, int durationInMin, DateTime firstDate, Frequency frequency, int frequencyNumber, int id,  DateTime created, DateTime modified, boolean deleted, String createdId, String modifiedId) {
        super(id, created, modified, deleted, createdId, modifiedId);
        this.name = name;
        this.description = description;
        this.responsible = responsible;
        this.difficulty = difficulty;
        this.durationInMin = durationInMin;
        this.firstDateInMs = firstDate.getMillis();
        this.frequency = frequency;
        this.frequencyNumber = frequencyNumber;
    }
    public CleanTask(String name, String description, String responsible, int difficulty, int durationInMin, DateTime firstDate, Frequency frequency, int frequencyNumber) {
        this(name, description, responsible, difficulty, durationInMin, firstDate, frequency, frequencyNumber, 0, null, null, false, null, null);
    }

    public CleanTask(String name, String description, String responsible, int difficulty, int durationInMin, DateTime firstDate) {
        this(name, description, responsible, difficulty, durationInMin, firstDate, Frequency.none, 0);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getResponsible() {
        return responsible;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getDurationInMin() {
        return durationInMin;
    }

    public DateTime getFirstDate() {
        return new DateTime(firstDateInMs);
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public int getFrequencyNumber() {
        return frequencyNumber;
    }

    @Override
    public String toString() {
        return "CleanTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", responsible='" + responsible + '\'' +
                ", difficulty=" + difficulty +
                ", durationInMin=" + durationInMin +
                ", firstDateInMs=" + firstDateInMs +
                ", frequency=" + frequency +
                ", frequencyNumber=" + frequencyNumber +
                '}';
    }
}
