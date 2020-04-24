package com.example.qwikpik.notification;

import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.example.qwikpik.R;

public class NotificationCreator {
    public static Notification createNotificationForMessage(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "42")
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle("QwikPik")
                .setContentText("Souvenir à revoir!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder.build();
    }

}
