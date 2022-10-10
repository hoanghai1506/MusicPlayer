package com.example.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class CreateNotification {
    public static final String CHANNEL_ID = "channel1";
    public static final String ACTION_PREVIOUS = "actionprevious";
    public static final String ACTION_NEXT = "actionnext";
    public static Notification notification;


    public static void createNotification(Context context, String track, int playbutton, int pos, String size){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context,"tag");
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),track.getImage());
        PendingIntent pendingIntentPrevious = null;
        int drw_previous;
        if(pos ==0 ){
            pendingIntentPrevious = null;
            drw_previous = 0;

        } else {
//            Intent intentPrevious = new Intent(context, NotificationActionService.class)

        }
        // create notification
        notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.music)
                .setContentTitle(track.getTitile())
                .setLargeIcon(icon)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .addAction(drw_previous,"Previous",pendingIntentPrevious)
                .addAction(playbutton,"Play",pendingIntentPrevious)
                .addAction(drw_next,"Next",pendingIntentPrevious)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0,1,2)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

            notificationManagerCompat.notify(1,notification);
    }

    }
}
