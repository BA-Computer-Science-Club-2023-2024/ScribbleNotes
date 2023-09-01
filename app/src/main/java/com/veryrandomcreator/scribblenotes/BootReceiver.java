package com.veryrandomcreator.scribblenotes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * The {@link BroadcastReceiver} responsible for responding to {@link Intent#ACTION_BOOT_COMPLETED} events.
 * <p>
 * This code follows the code examples found on the <a href="https://developer.android.com/guide/components/broadcasts">Android Developer Website</a>,
 * which contains more information on using Broadcast receivers
 */
public class BootReceiver extends BroadcastReceiver {
    /**
     * The default method used to respond to BroadcastReceiver actions.
     * This method checks for the correct event {@link Intent#ACTION_BOOT_COMPLETED}, and starts {@link NotificationService}
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) { // Checks for boot completed event
            Intent serviceIntent = new Intent(context, NotificationService.class); // Creates intent to launch service
            serviceIntent.setAction(NotificationService.START_SERVICE_ACTION); // Sets the action used by NotificationService, allowing it to bypass the toggle notification functionality
            context.startForegroundService(serviceIntent); // Starts the service
        }
    }
}
