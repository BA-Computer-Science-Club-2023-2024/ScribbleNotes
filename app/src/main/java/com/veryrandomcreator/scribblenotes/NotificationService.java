package com.veryrandomcreator.scribblenotes;

import android.Manifest;
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
import androidx.core.content.ContextCompat;

//https://developer.android.com/develop/ui/views/notifications/navigation
//https://developer.android.com/guide/components/foreground-services
public class NotificationService extends Service {
    public static final int NOTIFICATION_ID = 3;
    public static final String NOTIFICATION_CHANNEL_ID = "1";
    public static final int NOTIFICATION_ACTION_REQUEST_CODE = 2;

    public static final String START_SERVICE_ACTION = "com.veryrandomcreator.scribblenotes.START_SERVICE_ACTION";

    public static boolean isRunning = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning || (intent.getAction() != null && intent.getAction().equals(START_SERVICE_ACTION))) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Scribble Notes Notification Channel", NotificationManager.IMPORTANCE_LOW));

            Intent startActivityIntent = new Intent(this, MainActivity.class);

            NotificationCompat.Builder serverNotificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notifications_on)
                    .setContentTitle("Create new note")
                    .setContentText("Click to create new note")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOngoing(true)
                    .addAction(new NotificationCompat.Action(R.drawable.ic_notifications_on, "Create Note", PendingIntent.getActivity(this, NOTIFICATION_ACTION_REQUEST_CODE, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE)));
            Notification notification = serverNotificationBuilder.build();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(NOTIFICATION_ID, notification);
                startForeground(NOTIFICATION_ID, notification);
                isRunning = true;
            }
        } else {
            isRunning = false;
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}