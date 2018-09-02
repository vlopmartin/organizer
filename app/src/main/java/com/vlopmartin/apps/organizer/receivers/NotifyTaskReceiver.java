package com.vlopmartin.apps.organizer.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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

    public static int REQUEST = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        List<Task> taskList = Task.getListWithNotification(context);
        LocalDateTime now = LocalDateTime.now();
        for (Task task: taskList) {
            LocalDateTime dateTime = task.getDueDate().atTime(task.getNotificationTime());
            if (dateTime.isBefore(now) && !task.isNotified()) {
                NotificationHelper.notify(context, task);
                task.setNotified(true);
                task.save(context);
            }
        }
        schedule(context);
    }

    public static void schedule(Context context) {
        List<Task> taskList = Task.getListWithNotification(context);
        Intent intent = new Intent(context, NotifyTaskReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Get the nearest task with a date and a notification time
        Task nearestTask = null;
        for (Task task : taskList) {
            if (nearestTask == null) nearestTask = task;
            else {
                LocalDateTime taskDateTime = task.getDueDate().atTime(task.getNotificationTime());
                LocalDateTime nearestTaskDateTime = nearestTask.getDueDate().atTime(nearestTask.getNotificationTime());
                if (taskDateTime.isBefore(nearestTaskDateTime)) nearestTask = task;
            }
        }

        // If we found a task that needs to be notified...
        if (nearestTask != null) {
            LocalDateTime dateTime = nearestTask.getDueDate().atTime(nearestTask.getNotificationTime());
            LocalDateTime now = LocalDateTime.now();
            if (dateTime.isBefore(now)) {
                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    // This should never happen
                    e.printStackTrace();
                }
            } else {
                Duration in = Duration.between(now, dateTime);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + in.toMillis(), pendingIntent);
            }
        }
    }
}
