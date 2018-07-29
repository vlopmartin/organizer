package com.vlopmartin.apps.organizer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vlopmartin.apps.organizer.R;
import com.vlopmartin.apps.organizer.Task;
import com.vlopmartin.apps.organizer.TaskListAdapter;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public List<Task> taskList;
    public TaskListAdapter taskListAdapter;

    private static final int NEW_TASK_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Custom code starts here

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                //MainActivity.this.startActivity(intent);
                MainActivity.this.startActivityForResult(intent, NEW_TASK_REQUEST);
            }
        });

        RecyclerView mainListView = findViewById(R.id.main_list);
        mainListView.setLayoutManager(new LinearLayoutManager(this));

        taskList = Task.getTaskList(this.getApplicationContext());

        /*taskList = new ArrayList<Task>();
        taskList.add(new Task("Task 1", "Description 1"));
        taskList.add(new Task("Task 2", "Description 2"));
        taskList.add(new Task("Task 3", "Description 3"));*/

        taskListAdapter = new TaskListAdapter(taskList);
        mainListView.setAdapter(taskListAdapter);

    }

    public void addTask(Task newTask, int index) {
        newTask.save(this.getApplicationContext());
        taskList.add(index, newTask);
        taskListAdapter.notifyItemInserted(index);
    }

    public void addTask(Task newTask) {
        addTask(newTask, taskList.size());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_TASK_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                String taskName = data.getStringExtra(NewTaskActivity.TASK_NAME);
                String taskDescription = data.getStringExtra(NewTaskActivity.TASK_DESCRIPTION);
                Task newTask = new Task(0, taskName, taskDescription);
                this.addTask(newTask);
            }
        }
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

    @Override
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
    }
}
