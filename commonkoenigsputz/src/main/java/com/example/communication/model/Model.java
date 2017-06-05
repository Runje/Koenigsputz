package com.example.communication.model;

import java.util.List;

/**
 * Created by Thomas on 03.01.2017.
 */

public interface Model {
    void addTask(CleanTask cleanTask);

    void pause();

    void resume();

    void start();

    void enteredName(String name);

    List<CleanTask> getCleanPlan();
}
