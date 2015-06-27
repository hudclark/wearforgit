package com.octopusbeach.wearforgit;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by hudson on 6/27/15.
 */
public class NotificationListener extends WearableListenerService {

    private int notificationId = 1;

    public static final String NOTIFICATION_PATH = "/notification";
    public static final String NOTIFICATION_TIMESTAMP = "timestamp";
    public static final String NOTIFICATION_TITLE = "title";
    public static final String NOTIFICATION_CONTENT = "content";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                if (NOTIFICATION_PATH.equals(dataEvent.getDataItem().getUri().getPath())) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(dataEvent.getDataItem());
                    String title = dataMapItem.getDataMap().getString(NOTIFICATION_TITLE);
                    String content = dataMapItem.getDataMap().getString(NOTIFICATION_CONTENT);
                    sendNotification(title, content);
                }
            }
        }
    }

    private void sendNotification(String title, String content) {
        // Create the second page which will bring us to an activity.
        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingViewIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);
        Notification secondPage = new NotificationCompat.Builder(this)
                .extend(new NotificationCompat.WearableExtender()
                        .setDisplayIntent(pendingViewIntent)
                        .setCustomSizePreset(Notification.WearableExtender.SIZE_FULL_SCREEN))
                .build();


        Notification note = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_ALL)
                .extend(new NotificationCompat.WearableExtender()
                        .addPage(secondPage))
                .build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(notificationId++, note);
    }

}
