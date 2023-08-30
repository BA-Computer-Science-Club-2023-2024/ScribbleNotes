package com.veryrandomcreator.scribblenotes;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
//https://developer.android.com/develop/ui/views/notifications/navigation
//https://developer.android.com/guide/components/foreground-services
public class NotificationService extends Service {
    public static final int NOTIFICATION_ID = 3;
    public static final String NOTIFICATION_CHANNEL_ID = "1";

    public static final int NOTIFICATION_ACTION_REQUEST_CODE = 2;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Scribble Notes Notification Channel", NotificationManager.IMPORTANCE_DEFAULT));
        Intent startActivityIntent = new Intent(this, MainActivity.class);
        NotificationCompat.Builder serverNotificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_on)
                .setContentTitle("Create new note")
                .setContentText("Click to create new note")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .addAction(new NotificationCompat.Action(R.drawable.ic_notifications_on, "Create Note", PendingIntent.getActivity(this, NOTIFICATION_ACTION_REQUEST_CODE, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT)));
        Notification notification = serverNotificationBuilder.build();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(NOTIFICATION_ID, notification);
            startForeground(NOTIFICATION_ID, notification);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}