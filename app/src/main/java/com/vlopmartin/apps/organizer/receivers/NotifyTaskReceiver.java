package com.vlopmartin.apps.organizer.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.vlopmartin.apps.organizer.NotificationHelper;
import com.vlopmartin.apps.organizer.Task;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.ChronoField;

import java.util.List;

public class NotifyTaskReceiver extends BroadcastReceiver {

    private final static String TAG = "NotifyTaskReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Task task = Task.getById(context, intent.getLongExtra(CompleteTaskReceiver.TASK_ID, 0));
            Log.d(TAG, "Notifying task: " + task.getName());
            NotificationHelper.notify(context, task);
        } catch (Exception e) {
            Log.e(TAG, "Exception while notifying task: " + e.getMessage());
            Log.e(TAG, e.toString());
        }
    }
}
