package com.example.qwikpik.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.qwikpik.GalleryActivity;
import com.example.qwikpik.MainActivity;
import com.example.qwikpik.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class NotificationService extends Service {

    public static final String CHANNEL_ID = "NotificationServiceChannel";
    public static final String TAG = "NotificationService";
    NotificationManager notificationManager;
    LocationRequest mLocationRequest;
    static Location currentLocation ;
    Boolean requestingLocationUpdates;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    int idNotification = 2;

    @Override
    public void onCreate(){
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createChannelService();
        createChannelNotificationMessage();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(20000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        requestingLocationUpdates = true ;
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    currentLocation=location;
                    // ...
                }
                listenToPosition(currentLocation);
            }
        };
        setListener();
        startLocationUpdates();

    }

    public static Location getCurrentLocation() {
        return  currentLocation;
    }

    private void setListener(){
        fusedLocationClient.getLastLocation().addOnSuccessListener(new Activity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            currentLocation = location;
                        }
                    }
                });
    }

    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(mLocationRequest,
                locationCallback,
                Looper.getMainLooper());
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
        //listenToPosition();
        return START_STICKY;
    }


    private void listenToPosition(Location location){
        boolean notifSent=false;
        File[] pictures = getAllFilesInPictures();
        Location[] picturePositions = new Location[pictures.length];
        for (int i = 0; i < pictures.length; i++){
            String toDecrypt = pictures[i].getName();
            Location locate = new Location(String.valueOf(this));
            if(!toDecrypt.contains("_lat:")) {
                locate.setLatitude(0);
                locate.setLongitude(0);
            }
            else {
                locate.setLatitude(Double.parseDouble(toDecrypt.substring(toDecrypt.lastIndexOf("_lat:") + 5, toDecrypt.lastIndexOf(",lon:"))));
                locate.setLongitude(Double.parseDouble(toDecrypt.substring(toDecrypt.lastIndexOf(",lon:") + 5, toDecrypt.lastIndexOf(",date:"))));
            }
            picturePositions[i] = locate;

        }
        for (int i = 0; i < picturePositions.length; i++) {
            if(getDistance(location, picturePositions[i]) < 0.0005 && !notifSent){
                sendNotification(pictures[i].getName());
                notifSent = true;
            }
        }
    }

    private File[] getAllFilesInPictures() {
        String path = Environment.getExternalStorageDirectory().toString()+"/Android/data/com.example.qwikpik/files/Pictures";
        File directory = new File(path);
        File[] files = directory.listFiles();
        return files;
    }

    private double getDistance(Location location, Location position) {
        return Math.sqrt( Math.pow(location.getLatitude()-position.getLatitude(),2)+ Math.pow(location.getLongitude()-position.getLongitude(),2));
    }

    private void sendNotification(String pic_name){
        Notification notificationToSend = NotificationCreator.createNotificationForMessage(this,pic_name);
        notificationManager.notify(idNotification,notificationToSend);
        //idNotification++;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
