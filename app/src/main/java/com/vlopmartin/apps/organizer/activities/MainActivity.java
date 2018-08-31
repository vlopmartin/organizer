package com.vlopmartin.apps.organizer.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vlopmartin.apps.organizer.receivers.AlarmReceiver;
import com.vlopmartin.apps.organizer.NotificationHelper;
import com.vlopmartin.apps.organizer.R;
import com.vlopmartin.apps.organizer.Task;
import com.vlopmartin.apps.organizer.TaskListAdapter;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
        //implements NavigationView.OnNavigationItemSelectedListener

    public List<Task> taskList;
    public TaskListAdapter taskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        // Custom code starts here

        Resources resources = getResources();

        RecyclerView mainListView = findViewById(R.id.main_list);
        mainListView.setLayoutManager(new LinearLayoutManager(this));

        taskList = Task.getList(this.getApplicationContext());
        sortTaskList(taskList);

        taskListAdapter = new TaskListAdapter(taskList, resources);
        mainListView.setAdapter(taskListAdapter);

        NotificationHelper.createNotificationChannel(getApplicationContext());
        scheduleChecker();
    }

    protected void sortTaskList(List<Task> taskList) {
        Collections.sort(taskList, new Task.PriorityComparator());
        Collections.sort(taskList, new Task.DateComparator());
        Collections.sort(taskList, new Task.CurrentComparator());
    }

    public void onNewTask(View view) {
        Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
        MainActivity.this.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskList = Task.getList(this.getApplicationContext());
        sortTaskList(taskList);
        taskListAdapter.setTaskList(taskList);
        taskListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void scheduleChecker() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000, getResources().getInteger(R.integer.checking_interval), pendingIntent);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

}
