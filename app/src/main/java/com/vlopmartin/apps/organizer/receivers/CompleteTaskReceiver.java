package com.vlopmartin.apps.organizer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.vlopmartin.apps.organizer.NotificationHelper;
import com.vlopmartin.apps.organizer.R;
import com.vlopmartin.apps.organizer.Task;
import com.vlopmartin.apps.organizer.TaskCompletionObservable;
import com.vlopmartin.apps.organizer.activities.MainActivity;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.List;

public class CompleteTaskReceiver extends BroadcastReceiver {

    private final static String TAG = "CompleteTaskReceiver";
    public static final String TASK_ID = "com.vlopmartin.apps.organizer.task_id";
    public static int REQUEST = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        long taskId = intent.getLongExtra(TASK_ID, 0);
        Task task = Task.getById(context, taskId);
        if (task != null) {
            Log.d(TAG, "Completing task: " + task.getName());
            task.complete(context);
            TaskCompletionObservable.getInstance().completeTask(taskId);
        } else {
            Log.d(TAG, "Could not find task with ID " + String.valueOf(taskId));
        }
        NotificationManagerCompat.from(context).cancel((int) taskId);
    }
}
