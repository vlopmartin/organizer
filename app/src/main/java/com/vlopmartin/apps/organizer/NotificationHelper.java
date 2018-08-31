package com.vlopmartin.apps.organizer;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.app.NotificationManager;
import android.support.v4.app.NotificationManagerCompat;

import com.vlopmartin.apps.organizer.activities.TaskDetailsActivity;
import com.vlopmartin.apps.organizer.receivers.CompleteTaskReceiver;

public abstract class NotificationHelper {

    @TargetApi(26)
    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String id = context.getString(R.string.notification_channel_id);
            String name = context.getString(R.string.notification_channel_name);
            String description = context.getString(R.string.notification_channel_description);
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void notify(Context context, Task task) {
        String channelId = context.getString(R.string.notification_channel_id);
        Builder builder = new Builder(context, channelId)
                .setContentTitle(task.getName())
                .setContentText(task.getDescription())
                .setSmallIcon(R.drawable.ic_alarm_black_24dp);

        Intent intent = new Intent(context, CompleteTaskReceiver.class);
        intent.putExtra(TaskDetailsActivity.TASK_ID, task.getId());
        //Bundle extras = new Bundle();
        //extras.putLong(TaskDetailsActivity.TASK_ID, task.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, CompleteTaskReceiver.REQUEST, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_check_black_24dp, "Complete", pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int)task.getId(), builder.build());
    }
}
