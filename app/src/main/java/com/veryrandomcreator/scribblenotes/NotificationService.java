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

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

/**
 * A service to launch a notification that allows the user to open app.
 * <p>
 * The following code contains snippets found on the <a href="https://developer.android.com/develop/ui/views/notifications/navigation">Android Developer Website</a>.
 * Visit the website for more information about using notifications.
 * <p>
 * The code also contains snippets found on the <a href="https://developer.android.com/guide/components/foreground-services">Android Developer Website</a> for launching foreground services
 * For more information about creating and using foreground services visit the website.
 */
public class NotificationService extends Service {
    /**
     * The id for the notification
     */
    public static final int NOTIFICATION_ID = 3;

    /**
     * The channel id
     */
    public static final String NOTIFICATION_CHANNEL_ID = "1";

    /**
     * Request code for activity launch action
     */
    public static final int NOTIFICATION_ACTION_REQUEST_CODE = 2;

    /**
     * The action identifier for the action to start the notification
     */
    public static final String START_SERVICE_ACTION = "com.veryrandomcreator.scribblenotes.START_SERVICE_ACTION";

    /**
     * Stores the state of the notification service, independent of the instance
     */
    public static boolean isRunning = false;

    /**
     * No need of this method
     *
     * @param intent The Intent that was used to bind to this service,
     * as given to {@link android.content.Context#bindService
     * Context.bindService}.  Note that any extras that were included with
     * the Intent at that point will <em>not</em> be seen here.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * The start method, receiving the event which starts the service
     *
     * @param intent The Intent supplied to {@link android.content.Context#startService},
     * as given.  This may be null if the service is being restarted after
     * its process has gone away, and it had previously returned anything
     * except {@link #START_STICKY_COMPATIBILITY}.
     * @param flags Additional data about this start request.
     * @param startId A unique integer representing this specific request to
     * start.  Use with {@link #stopSelfResult(int)}.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning || (intent.getAction() != null && intent.getAction().equals(START_SERVICE_ACTION))) { // Checks the state of the toggle or starts the notification of the command specifies to
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this); // The notification manager to manage posting notifications
            notificationManager.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Scribble Notes Notification Channel", NotificationManager.IMPORTANCE_LOW)); // The channel for the notification

            Intent startActivityIntent = new Intent(this, MainActivity.class); // The intent to start the app
            NotificationCompat.Builder serverNotificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID) // Sets the channel for the notification
                    .setSmallIcon(R.drawable.ic_notifications_on) // Sets the notification icon
                    .setContentTitle("Create new note") // Sets the notification title
                    .setContentText("Click to create new note") // Sets the notification content text
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Sets the priority of the notification
                    .setOngoing(true) // Makes the notification unable to be swiped away
                    .addAction(new NotificationCompat.Action(R.drawable.ic_notifications_on, // Sets action's icon
                            "Create Note", // Sets the action's text
                            PendingIntent.getActivity(this, NOTIFICATION_ACTION_REQUEST_CODE, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE)) // Sets the action
                    );
            Notification notification = serverNotificationBuilder.build(); // Builds the notification
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) { // Checks the notification permission
                notificationManager.notify(NOTIFICATION_ID, notification); // Sends the notification
                startForeground(NOTIFICATION_ID, notification); // Links the pushed notification to the service to allow it to continue running as a foreground service
                isRunning = true; // Updates the state of the notification service
            }
        } else {
            isRunning = false; // Updates the state of the notification service
            stopSelf(); // Stops the service
        }
        return super.onStartCommand(intent, flags, startId);
    }
}