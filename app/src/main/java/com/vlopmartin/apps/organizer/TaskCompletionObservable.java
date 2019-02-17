package com.vlopmartin.apps.organizer;

import java.util.Observable;

public class TaskCompletionObservable extends Observable {

    private static TaskCompletionObservable instance = new TaskCompletionObservable();

    public static TaskCompletionObservable getInstance() {
        return instance;
    }

    private TaskCompletionObservable() {}

    public void completeTask(long id) {
        synchronized (this) {
            setChanged();
            notifyObservers(id);
        }
    }
}
