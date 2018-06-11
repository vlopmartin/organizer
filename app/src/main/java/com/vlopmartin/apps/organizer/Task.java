package com.vlopmartin.apps.organizer;

import android.app.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Task implements Serializable {

    public static final String tasksDirName = "tasks";

    private int id;
    private String name;
    private String description;

    public static List<Task> readTasks(File filesDir) throws IOException, ClassNotFoundException {
        List<Task> ret = new ArrayList<Task>();

        File tasksDir = new File(filesDir, tasksDirName);
        for (File file : tasksDir.listFiles()) {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Task task = (Task) ois.readObject();
            ois.close();
            fis.close();

            ret.add(task);
        }

        return ret;
    }

    public Task(String name, String description) {
        setName(name);
        setDescription(description);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
