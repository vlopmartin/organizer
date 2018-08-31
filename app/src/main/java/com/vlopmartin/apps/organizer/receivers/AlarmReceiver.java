package com.vlopmartin.apps.organizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.TemporalUnit;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    public static int REQUEST = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        List<Task> taskList = Task.getList(context);
        int interval = context.getResources().getInteger(R.integer.checking_interval);
        LocalDateTime now = LocalDateTime.now();
        for (Task task: taskList) {
            LocalDate dueDate = task.getDueDate();
            LocalTime notificationTime = task.getNotificationTime();
            if (dueDate != null && notificationTime != null) {
                LocalDateTime dateTime = dueDate.atTime(notificationTime);
                if (dateTime.isBefore(now) && !task.isNotified()) {
                    NotificationHelper.notify(context, task);
                    task.setNotified(true);
                    task.save(context);
                }
            }
        }
    }
}
