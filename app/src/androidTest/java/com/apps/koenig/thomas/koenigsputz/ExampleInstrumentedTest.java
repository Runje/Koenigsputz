package com.apps.koenig.thomas.koenigsputz;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.apps.koenig.thomas.koenigsputz.AppModel.Database.PutzDatabase;
import com.example.communication.model.CleanTask;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private PutzDatabase database;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        database = new PutzDatabase(context);
        database.resetAll();
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.apps.koenig.thomas.koenigsputz", appContext.getPackageName());
    }

    @Test
    public void changeId() {

        int oldId = 1;
        int newId = 5;
        database.addCleanTask(CleanTasks.createWithIndex(oldId));
        assertTrue(database.doesExistWithId(oldId));
        assertFalse(database.doesExistWithId(newId));

        database.changeCleanTaskId(oldId, newId);

        assertFalse(database.doesExistWithId(oldId));
        assertTrue(database.doesExistWithId(newId));
    }

    @Test
    public void comingCleanTasks() {
        database.addCleanTask(CleanTasks.createWithDate(1, DateTime.now()));
        database.addCleanTask(CleanTasks.createWithDate(2, DateTime.now().plusDays(2).withTimeAtStartOfDay()));
        database.addCleanTask(CleanTasks.createWithDate(3, DateTime.now().plusDays(3)));
        database.addCleanTask(CleanTasks.createWithDate(4, DateTime.now().minusDays(2)));

        List<CleanTask> comingCleanTasks = database.getComingCleanTasks(2);
        assertEquals(2, comingCleanTasks.size());
        assertIsInList(1, comingCleanTasks);
        assertIsInList(2, comingCleanTasks);

    }

    private void assertIsInList(int id, List<CleanTask> cleanTasks) {
        boolean found = false;
        for (int i = 0; i < cleanTasks.size(); i++) {
            CleanTask cleanTask = cleanTasks.get(i);
            if (id == cleanTask.getId()) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }
}
