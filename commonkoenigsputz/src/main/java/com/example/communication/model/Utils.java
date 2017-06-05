package com.example.communication.model;

/**
 * Created by Thomas on 08.01.2017.
 */

public class Utils {

    public static boolean isCleanTaskValid(CleanTask cleanTask) {
        if (cleanTask == null) {
            return false;
        }

        if (cleanTask.getName().equals("")) {
            return false;
        }

        if (cleanTask.getDifficulty() < CleanTask.MinDifficulty || cleanTask.getDifficulty() > CleanTask.MaxDifficulty) {
            return false;
        }

        if (cleanTask.getDurationInMin() <= 0) {
            return false;
        }

        return true;
    }
}
