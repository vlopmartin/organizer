package com.vlopmartin.apps.organizer;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public static final String PREFERENCES_NAME = "file_manager_preferences";
    public static final String DIR_TASKS = "tasks";

    private static final String KEY_TASK_COUNTER = "task_counter";
    private static final int DEF_TASK_COUNTER = 1;

    private Context context;
    private SharedPreferences sharedPreferences;
    private File tasksDir;

    public FileManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.tasksDir = context.getDir(DIR_TASKS, Context.MODE_PRIVATE);
    }

    public int getTaskCounter() {
        return sharedPreferences.getInt(KEY_TASK_COUNTER, DEF_TASK_COUNTER);
    }

    public void setTaskCounter(int taskCounter) {
        sharedPreferences.edit().putInt(KEY_TASK_COUNTER, taskCounter).apply();
    }

    public int nextTaskCounter() {
        int taskCounter = getTaskCounter();
        setTaskCounter(++taskCounter);
        return taskCounter;
    }

    public void saveTask(Task task) throws IOException {
        File file;
        int id = task.getId();
        if (id == 0) {
            id = nextTaskCounter();
            task.setId(id);
            String filename = String.valueOf(id);
            file = new File(tasksDir, filename);
            file.createNewFile();
        }
        else {
            String filename = String.valueOf(id);
            file = new File(tasksDir, filename);
        }
        FileOutputStream fos = new FileOutputStream(file, false);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(task);
        oos.close();
        fos.close();
    }

    public void deleteTask(Task task) /*throws FileNotFoundException*/ {
        int id = task.getId();
        if (id == 0) {
            // TODO: Logic if trying to save nonexistent task?
        }
        else {
            String filename = String.valueOf(id);
            File file = new File(tasksDir, filename);
            file.delete();
        }
    }

    public List<Task> getTaskList() throws IOException {
        List<Task> taskList = new ArrayList<Task>();
        File[] filesList = tasksDir.listFiles();
        if (filesList != null) { // NullPointerException if there are no files??
            for (File file : tasksDir.listFiles()) {
                Task task;
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                try {
                    task = (Task) ois.readObject();
                    taskList.add(task);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    // TODO: Should we control the case where the object is of an unknown class?
                }
                ois.close();
                fis.close();
            }
        }
        return taskList;
    }

}
