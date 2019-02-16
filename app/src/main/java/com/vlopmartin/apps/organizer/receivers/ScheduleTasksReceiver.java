package com.vlopmartin.apps.organizer.receivers;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.vlopmartin.apps.organizer.Task;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

import java.util.List;

public class ScheduleTasksReceiver extends BroadcastReceiver {

    private final static String TAG = "ScheduleTasksReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        scheduleTasks(context);
    }

    public static void scheduleTasks(Context context) {
        List<Task> tasks = Task.getListWithNotification(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        LocalDateTime now = LocalDateTime.now();
        for (Task task : tasks) {
            Intent notifyIntent = new Intent(context, NotifyTaskReceiver.class);
            notifyIntent.putExtra(CompleteTaskReceiver.TASK_ID, task.getId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)task.getId(), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            LocalDateTime dateTime = task.getDueDate().atTime(task.getNotificationTime());
            if (dateTime.isBefore(now)) {
                try {
                    Log.d(TAG, "Task is in the past, sending intent now");
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    Log.e(TAG, "The intent was cancelled!");
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "Scheduling task for: " + dateTime.toString());
                Duration in = Duration.between(now, dateTime);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + in.toMillis(), pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + in.toMillis(), pendingIntent);
                }
            }
        }
    }
}
