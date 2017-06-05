package com.apps.koenig.thomas.koenigsputz.AppModel;


import android.content.Context;

import com.apps.koenig.thomas.koenigsputz.AppModel.Database.PutzDatabase;
import com.apps.koenig.thomas.koenigsputz.AppModel.communication.Connection;
import com.apps.koenig.thomas.koenigsputz.AppModel.communication.ConnectionEventListener;
import com.apps.koenig.thomas.koenigsputz.AppModel.communication.ServerConnection;
import com.apps.koenig.thomas.koenigsputz.Koenigsputz;
import com.apps.koenig.thomas.koenigsputz.Presenter.OverviewPresenter;
import com.apps.koenig.thomas.koenigsputz.R;
import com.example.communication.model.CleanTask;
import com.example.communication.model.Frequency;
import com.example.communication.model.communication.messages.CleanTasksAnsMessage;
import com.example.communication.model.communication.messages.CleanTasksMessage;
import com.example.communication.model.communication.messages.KoenigsputzMessage;
import com.example.communication.model.database.Database;
import com.example.communication.model.Model;
import com.example.communication.model.communication.messages.AskForUpdatesMessage;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Thomas on 03.01.2017.
 */
public class PutzModel implements Model, ConnectionEventListener {
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private final Database database;
    private Context context;
    private Connection connection;
    private OverviewPresenter presenter;
    private ScheduledExecutorService service;

    private boolean changeAllowed = true;

    public PutzModel(Context context, OverviewPresenter presenter) {
        database = new PutzDatabase(context);
        this.context = context;
        this.connection = new ServerConnection(Installation.id(context));
        this.presenter = presenter;
        connection.setOnConnectionEventListener(this);

        /**
         * TEST CODE
         */
        createDemoDatabase();
    }

    private void createDemoDatabase() {
        PutzDatabase db = (PutzDatabase) database;
        db.resetAll();
        db.addCleanTask(new CleanTask("Bad putzen", "Beide Bäder +  Küche wischen", "Thomas", 3, 30, DateTime.now(), Frequency.weekly, 1,-1, DateTime.now(), DateTime.now(),false, Installation.id(context), Installation.id(context)));
        db.addCleanTask(new CleanTask("Programmieren", "Putzapp", "Thomas", 1, 30, DateTime.now(), Frequency.none, 0, -1, DateTime.now(), DateTime.now(),false, Installation.id(context), Installation.id(context)));
        db.addCleanTask(new CleanTask("Saugen", "Alle Räume", "Milena", 1, 30, DateTime.now().plusDays(1), Frequency.weekly, 1, -1, DateTime.now(), DateTime.now(),false, Installation.id(context), Installation.id(context)));

    }

    @Override
    public void addTask(CleanTask cleanTask) {
       logger.info("Add cleanTask to Database: " + cleanTask);

        cleanTask.setCreatedFrom(Installation.id(context));
        cleanTask.setLastChangeFrom(Installation.id(context));
        cleanTask.setLastModifiedDate(DateTime.now());
        cleanTask.setInsertDate(DateTime.now());
        database.addCleanTask(cleanTask);
        ArrayList<CleanTask> cleanTasks = new ArrayList<>();
        cleanTasks.add(cleanTask);
        sendMessage(new CleanTasksMessage(cleanTasks));

    }

    private void logAllCleanTasks() {
        List<CleanTask> allCleanTasks = database.getAllCleanTasks();
        for (int i = 0; i < allCleanTasks.size(); i++) {
            CleanTask task = allCleanTasks.get(i);
            logger.info(task.toString());
        }
    }



    private void sendMessage(KoenigsputzMessage message) {
        connection.sendKoenigsputzMessage(message);
    }

    @Override
    public void pause() {
        connection.disconnect();
        service.shutdown();
    }

    @Override
    public void resume() {
        service = Executors.newScheduledThreadPool(1);
        Runnable tryConnect = new Runnable() {
            @Override
            public void run() {
                if (!connection.isConnected()) {
                    connection.connect();
                }
            }
        };

        service.scheduleAtFixedRate(tryConnect, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void start() {
        String name = Koenigsputz.getName(context);
        if (name.equals(Koenigsputz.NO_NAME))
        {
            // first time

            //TODO Overview Intro

            presenter.askForNameOrImportFile();
        }
        else {
            presenter.sayHello(name);
        }
    }

    @Override
    public void enteredName(String name) {
        if (name.equals("") || name.equals(Koenigsputz.NO_NAME))        {
            presenter.showError(R.string.enter_name);
            presenter.askForNameOrImportFile();
        }
        else {
            Koenigsputz.saveName(name, context);
            presenter.sayHello(name);
            connection.sendKoenigsputzMessage(new AskForUpdatesMessage(name, Koenigsputz.getLastSyncDate(context)));
        }
    }

    @Override
    public List<CleanTask> getCleanPlan() {
        logAllCleanTasks();
        int days = 7;
        List<CleanTask> cleanTasks = database.getComingCleanTasks(days);
        List<CleanTask> frequentTask = database.getAllFrequentTasks();
        cleanTasks.addAll(getNextCleanTasks(days, frequentTask));
        return cleanTasks;
    }

    private List<CleanTask> getNextCleanTasks(int days, List<CleanTask> frequentTasks) {
        List<CleanTask> cleanTasks = new ArrayList<>();
        for (int i = 0; i < frequentTasks.size(); i++) {
            CleanTask frequentTask = frequentTasks.get(i);
            List<DateTime> dueDates = getDueDatesUntil(frequentTask, DateTime.now().plusDays(days));
            for (int j = 0; j < dueDates.size(); j++) {
                DateTime dateTime = dueDates.get(j);
                cleanTasks.add(frequentTaskToCleanTask(frequentTask, dateTime));
            }
        }

        return cleanTasks;
    }

    private CleanTask frequentTaskToCleanTask(CleanTask frequentTask, DateTime dateTime) {
        return new CleanTask(frequentTask.getName(), frequentTask.getDescription(), frequentTask.getResponsible(), frequentTask.getDifficulty(), frequentTask.getDurationInMin(), dateTime);
    }

    public static List<DateTime> getDueDatesUntil(CleanTask frequentTask, DateTime until)
    {
        assert frequentTask.getFrequency() != Frequency.none;
        List<DateTime> times = new ArrayList<>();
        // set hour to 12 to avoid switching days because of different timezones(summer time)
        int oldHour = frequentTask.getFirstDate().getHourOfDay();
        DateTime firstDate = frequentTask.getFirstDate().withHourOfDay(12);
        if (firstDate.isBefore(until))
        {
            times.add(firstDate.withHourOfDay(oldHour));
        }

        int i = 1;
        while (true)
        {
            if (frequentTask.getFrequencyNumber() == 0)
            {
                break;
            }

            DateTime nextDate = null;
            switch (frequentTask.getFrequency())
            {
                case daily:
                    nextDate = firstDate.plusDays(i * frequentTask.getFrequencyNumber());
                    break;
                case weekly:
                    nextDate = firstDate.plusWeeks(i * frequentTask.getFrequencyNumber());
                    break;
                case monthly:
                    nextDate = firstDate.plusMonths(i * frequentTask.getFrequencyNumber());
                    break;
                case yearly:
                    nextDate = firstDate.plusYears(i * frequentTask.getFrequencyNumber());
                    break;
            }

            if (nextDate.isBefore(until))
            {
                times.add(nextDate.withHourOfDay(oldHour));
            } else
            {
                break;
            }

            i++;
        }

        return times;
    }

    @Override
    public void onConnectionStatusChange(boolean connected) {
        presenter.setConnected(connected);
        logger.info(connected ? "CONNECTED" : "NOT CONNECTED");
        if (connected)
        {
            /*List<CleanTask> allCleanTasks = database.getAllCleanTasks();
            connection.sendKoenigsputzMessage(new CleanTasksMessage(allCleanTasks));*/
            String name = Koenigsputz.getName(context);
            if (!name.equals(Koenigsputz.NO_NAME)) {
                connection.sendKoenigsputzMessage(new AskForUpdatesMessage(name, Koenigsputz.getLastSyncDate(context)));
            }

            // TODO: Send unanswered messages
        }

    }

    @Override
    public void onReceiveMessage(KoenigsputzMessage message) {
        // TODO: What if changes are not allowed? Wait and try again!
        changeAllowed = false;
        switch(message.getName()) {
            case CleanTasksMessage.NAME:
                CleanTasksMessage cleanTasksMessage = (CleanTasksMessage) message;
                updateCleanTasks(cleanTasksMessage.getCleanTasks());
                break;
            case CleanTasksAnsMessage.NAME:
                CleanTasksAnsMessage cleanTasksAnsMessage = (CleanTasksAnsMessage) message;
                checkIds(cleanTasksAnsMessage.getOldIds(), cleanTasksAnsMessage.getNewIds());
                sendServerChangesSince(cleanTasksAnsMessage.getSyncTimestamp());
                break;
            default:
                logger.error("Unknown Message: " + message.getName());
        }

        changeAllowed = true;
    }

    private void sendServerChangesSince(DateTime dateTime) {
        // TODO
        // send MY changes since dateTime
        List<CleanTask> cleanTasks = database.getChangesSince(dateTime, Installation.id(context));
        sendMessage(new CleanTasksMessage(cleanTasks));
    }

    private void updateCleanTasks(List<CleanTask> cleanTasks) {
        for (int i = 0; i < cleanTasks.size(); i++) {
            CleanTask cleanTask = cleanTasks.get(i);

            CleanTask oldCleanTask = database.getCleanTaskWithId(cleanTask.getId());
            if (oldCleanTask != null) {
                // overwrite old entry
                database.overwrite(cleanTask);
                // overwrite or new entry?
                if (!(oldCleanTask.getInsertDate().equals(cleanTask.getInsertDate()) && oldCleanTask.getCreatedFrom().equals(cleanTask.getCreatedFrom()))) {
                    //change id of old entry because it is another entry
                    // force to generate new id
                    oldCleanTask.setId(-1);
                    //change id from old entry
                    database.addCleanTask(oldCleanTask);
                }
            } else {
                database.addCleanTask(cleanTask);
            }
        }
    }



    public boolean isChangeAllowed() {
        return changeAllowed;
    }

    private void checkIds(int[] oldIds, int[] newIds) {
        for (int i = 0; i < oldIds.length; i++) {
            int oldId = oldIds[i];
            int newId = newIds[i];
            if (oldId != newId) {
                database.changeCleanTaskId(oldId, newId);
            }
        }
    }

}
