package com.apps.koenig.thomas.koenigsputz.Presenter;

import android.content.Context;
import android.support.annotation.StringRes;

import com.apps.koenig.thomas.koenigsputz.R;
import com.apps.koenig.thomas.koenigsputz.View.NullOverview;
import com.apps.koenig.thomas.koenigsputz.View.Overview;
import com.apps.koenig.thomas.koenigsputz.View.RecyclerView.HeaderItem;
import com.apps.koenig.thomas.koenigsputz.View.RecyclerView.Item;
import com.apps.koenig.thomas.koenigsputz.View.RecyclerView.ListItem;
import com.example.communication.model.CleanTask;
import com.example.communication.model.Model;
import com.apps.koenig.thomas.koenigsputz.AppModel.PutzModel;
import com.example.communication.model.Utils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Thomas on 10.11.2016.
 */
public class OverviewPresenterImpl implements OverviewPresenter {

    private Overview view;
    private final Model model;
    private Context context;
    private Pages page;

    public OverviewPresenterImpl(Context context) {
        this.view = new NullOverview();
        this.model = new PutzModel(context, this);
        this.context = context;
        page = Pages.Overview;
    }

    @Override
    public void started() {
        model.start();
    }

    @Override
    public void attachView(Overview overview) {
        this.view = overview;
        model.resume();
    }

    @Override
    public void detachView() {
        this.view = new NullOverview();
        model.pause();
    }

    @Override
    public void askForNameOrImportFile() {
        view.askForNameOrImportFile();
    }

    @Override
    public void sayHello(String name) {
        view.showToast("Hello " + name);
        view.setName(name);
    }

    @Override
    public void showError(@StringRes int enter_name) {
        view.showToast(enter_name);
    }

    @Override
    public void setConnected(boolean connected) {
        view.showConnected(connected);
    }


    @Override
    public void enteredName(String name) {

        model.enteredName(name.trim());
    }

    @Override
    public void importFile() {
        // TODO:
        view.showToast(R.string.not_implemented);
        view.askForNameOrImportFile();
    }

    @Override
    public void clickedPage(Pages page) {
        switch (page) {
            case Overview:
                view.showOverview();
                break;
            case AddTask:
                view.showAddNewTask();
                break;
            case CleanPlan:
                showCleanPlan();

                break;
            case Template:
                view.showTemplate();
                break;
            case Statistic:
                view.showStatistic();
                break;
        }
    }

    private void showCleanPlan() {
        List<CleanTask> cleanTaskList = model.getCleanPlan();
        List<ListItem> cleanPlan = cleanTasksToCleanPlan(cleanTaskList);
        view.showCleanPlan(cleanPlan);
    }

    private List<ListItem> cleanTasksToCleanPlan(List<CleanTask> cleanTaskList) {
        Collections.sort(cleanTaskList, new Comparator<CleanTask>()
        {
            @Override
            public int compare(CleanTask lhs, CleanTask rhs)
            {
                return lhs.getFirstDate().compareTo(rhs.getFirstDate());
            }
        });

        List<ListItem> cleanPlan = new ArrayList<>();
        DateTime.Property day = DateTime.now().dayOfMonth();

        for (int i = 0; i < cleanTaskList.size(); i++) {
            CleanTask cleanTask = cleanTaskList.get(i);

            int diff = cleanTask.getFirstDate().dayOfMonth().get() - day.get();
            if (diff != 0 || i == 0)
            {
                day = cleanTask.getFirstDate().dayOfMonth();
                cleanPlan.add(new HeaderItem(dayToString(cleanTask.getFirstDate())));
            }

            cleanPlan.add(new Item(cleanTask));
        }
        return cleanPlan;
    }

    private String dayToString(DateTime dateTime) {
        int day = dateTime.getDayOfMonth();
        int today = DateTime.now().getDayOfMonth();
        if (day == today) {
            return context.getString(R.string.today);
        }

        if (day == DateTime.now().plusDays(1).getDayOfMonth()) {
            return context.getString(R.string.tomorrow);
        }

        return dateTime.dayOfWeek().getAsText();
    }

    @Override
    public void clickedFab() {
        switch (page) {
            case Overview:
                view.showAddNewTask();
                page = Pages.AddTask;
                break;
            case AddTask:
                CleanTask cleanTask = view.getNewTask();
                boolean valid = Utils.isCleanTaskValid(cleanTask);
                if (valid) {
                    model.addTask(cleanTask);
                    showCleanPlan();

                    page = Pages.CleanPlan;
                }
                else {
                    view.showToast("Invalid Task");
                }
                break;
        }

    }


}
