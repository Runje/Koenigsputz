package com.apps.koenig.thomas.koenigsputz.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.koenig.thomas.koenigsputz.Presenter.OverviewPresenter;
import com.apps.koenig.thomas.koenigsputz.Presenter.OverviewPresenterImpl;
import com.apps.koenig.thomas.koenigsputz.Presenter.Pages;
import com.apps.koenig.thomas.koenigsputz.R;
import com.apps.koenig.thomas.koenigsputz.View.RecyclerView.ListItem;
import com.example.communication.model.CleanTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OverviewActivity extends AppCompatActivity implements Overview , NavigationView.OnNavigationItemSelectedListener, AddTaskFragment.OnAddTaskListener {

    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private static final String ADD_TASK_FRAGMENT_TAG = "ADD_TASK";
    private OverviewPresenter presenter;
    private int pageId;
    private TextView textConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logger.debug("onCreate");
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickFab();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textConnected = (TextView) findViewById(R.id.text_connected);
        showConnected(false);
        presenter = new OverviewPresenterImpl(this);
        presenter.started();

    }

    private void clickFab() {
        presenter.clickedFab();
    }

    @Override
    public void askForNameOrImportFile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Name);
        //builder.setMessage(R.string.enter_name_or_import);
        final View layout = LayoutInflater.from(this).inflate(R.layout.enter_name, null);
        builder.setView(layout);
        final EditText editName = (EditText) layout.findViewById(R.id.editTextName);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editName.getText().toString();
                presenter.enteredName(name);
            }
        });

        builder.setNegativeButton(R.string.import_file, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.importFile();
            }
        });
        builder.show();

    }


    @Override
    public void showToast(int stringIndex) {
        showToast(getString(stringIndex));
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setName(String name) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView textName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewName);
        textName.setText(name);
    }

    @Override
    public void showCleanPlan(List<ListItem> cleanPlan) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CleanPlanFragment fragment = new CleanPlanFragment();
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList(CleanPlanFragment.ARG_CLEAN_PLAN, (ArrayList<? extends Parcelable>) cleanPlan);
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_overview, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void showAddNewTask() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddTaskFragment fragment = new AddTaskFragment();

        fragmentTransaction.replace(R.id.content_overview, fragment, ADD_TASK_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    @Override
    public CleanTask getNewTask() {
        // get AddTaskFragment
        AddTaskFragment addTaskFragment = (AddTaskFragment) getSupportFragmentManager().findFragmentByTag(ADD_TASK_FRAGMENT_TAG);

        if (addTaskFragment == null)
        {
            showToast("addTaskFragment not found");
        }
        else {
            CleanTask cleanTask = addTaskFragment.getCleanTask();
            showToast("CleanTask: " + cleanTask.toString());
            return cleanTask;
        }

        return null;
    }

    @Override
    public void showConnected(final boolean connected) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textConnected.setText(connected ? R.string.connected : R.string.not_connected);
                textConnected.setTextColor(connected ? Color.GREEN : Color.RED);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        pageId = id;
        if (id == R.id.nav_overview) {
            presenter.clickedPage(Pages.Overview);
        } else if (id == R.id.nav_clean_plan) {
            presenter.clickedPage(Pages.CleanPlan);
            pageId = id;
        } else if (id == R.id.nav_new_task) {
            presenter.clickedPage(Pages.AddTask);
        } else if (id == R.id.nav_template) {
            presenter.clickedPage(Pages.Template);
        } else if (id == R.id.nav_statistic) {
            presenter.clickedPage(Pages.Statistic);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    protected void onPause() {
        super.onPause();
        this.presenter.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.presenter.attachView(this);
    }

    @Override
    public String[] getResponsibleChoices() {
        // TODO: forward to model and get from Server/Database
        return new String[] { "Thomas", "Milena", "Zufall"};
    }

    @Override
    public void showOverview() {

    }

    @Override
    public void showTemplate() {

    }

    @Override
    public void showStatistic() {

    }
}
