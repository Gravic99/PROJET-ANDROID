package com.example.qwikpik.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.qwikpik.GalleryActivity;
import com.example.qwikpik.R;

public class NotificationCreator {
    public static Notification createNotificationForMessage(Context context, String text){
        Intent notificationIntent = new Intent(context, GalleryActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "42")
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle("QwikPik")
                .setContentText("Souvenir à revoir! La photo : "+text.substring(0,text.indexOf("_lat:"))+" a été prise près d'ici!")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder.build();
    }

}
