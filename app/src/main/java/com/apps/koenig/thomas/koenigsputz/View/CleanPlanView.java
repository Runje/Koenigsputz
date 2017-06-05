package com.apps.koenig.thomas.koenigsputz.View;

import com.apps.koenig.thomas.koenigsputz.View.RecyclerView.ListItem;
import com.example.communication.model.CleanTask;

import java.util.List;

/**
 * Created by Thomas on 10.11.2016.
 */

public interface CleanPlanView {
    void showCleanTasks(List<ListItem> cleanTaskList);
}
