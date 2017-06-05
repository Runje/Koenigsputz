package com.apps.koenig.thomas.koenigsputz.View;

import android.content.Context;

import com.apps.koenig.thomas.koenigsputz.View.RecyclerView.ListItem;
import com.example.communication.model.CleanTask;

import java.util.List;

/**
 * Created by Thomas on 10.11.2016.
 */
public interface Overview {
    void askForNameOrImportFile();

    void showToast(int stringIndex);

    void showToast(String text);

    void setName(String name);

    void showCleanPlan(List<ListItem> cleanPlan);

    void showAddNewTask();

    CleanTask getNewTask();

    void showConnected(boolean connected);

    void showOverview();

    void showTemplate();

    void showStatistic();
}
