package com.example.qwikpik.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.qwikpik.GalleryActivity;
import com.example.qwikpik.MainActivity;
import com.example.qwikpik.R;

public class NotificationService extends Service {

    public static final String CHANNEL_ID = "NotificationServiceChannel";
    public static final String TAG = "NotificationService";
    NotificationManager notificationManager;
    int idNotification = 2;

    @Override
    public void onCreate(){
        super.onCreate();
        createChannelService();
        createChannelNotificationMessage();
    }

    private void createChannelService(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelID = CHANNEL_ID;
            CharSequence channelName = "NotificationService";
            String channelDescription = "Notification service";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID,channelName,channelImportance);
            channel.setDescription(channelDescription);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createChannelNotificationMessage(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelID = "42";
            CharSequence channelName = "NotificationMessage";
            String channelDescription = "Notification message";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID,channelName,channelImportance);
            channel.setDescription(channelDescription);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service Qwick Pik ;)")
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentIntent(pendingIntent)
                .setContentText("Permet d'avoir des notification pour vous rappeler des souvenirs!")
                .build();
        startForeground(1,notification);
        listenToPosition();
        return START_STICKY;
    }

    private void listenToPosition(){
        sendNotification();
    }

    private void sendNotification(){
        Notification notificationToSend = NotificationCreator.createNotificationForMessage(this);
        notificationManager.notify(idNotification,notificationToSend);
        idNotification++;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
