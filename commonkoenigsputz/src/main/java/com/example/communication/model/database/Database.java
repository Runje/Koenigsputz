package com.example.communication.model.database;

import com.example.communication.model.CleanTask;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Thomas on 06.01.2017.
 */

public interface Database {

    void addCleanTask(CleanTask cleanTask);

    List<CleanTask> getAllCleanTasks();

    void changeCleanTaskId(int oldId, int newId);

    boolean cleanTaskIdExists(int id);

    CleanTask getCleanTaskWithId(int id);

    void overwrite(CleanTask cleanTask);

    List<CleanTask> getChangesSince(DateTime dateTime, String userId);

    List<CleanTask> getComingCleanTasks(int days);

    List<CleanTask> getAllFrequentTasks();
}
