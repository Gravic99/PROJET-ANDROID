package com.example.qwikpik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class Menu extends AppCompatActivity {
    LocationRequest mLocationRequest;
    Button goToGalleryButton;
    Button goToCameraButton;
    Location currentLocation ;
    Boolean requestingLocationUpdates;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ColorText();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

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
            }
        };

        goToCameraButton = findViewById(R.id.btn_TakeAPik);
        goToGalleryButton = findViewById(R.id.btn_Gallery);
        setListener();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Toast.makeText(this,"Veuillez redémarrer l'application pour assurer son bon fonctionnement.",Toast.LENGTH_LONG).show();
        System.exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (requestingLocationUpdates) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private void ColorText() {
        TextView textView = findViewById(R.id.textView_title);
        String text = "<font color=#6AEE6A>Q</font>" +
                "<font color=#F1A247>W</font>" +
                "<font color=#763C76>I</font>" +
                "<font color=#3535B9>K</font>" +
                "<font > </font>" +
                "<font color=#BC2B2D>P</font>" +
                "<font color=#763C76>I</font>" +
                "<font color=#3535B9>K</font>";
        textView.setText(Html.fromHtml(text));
    }

    private void setListener() {
        goToCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(CameraActivity.class);
            }
        });
        goToGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(GalleryActivity.class);
            }
        });
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
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

    private void goToActivity(Class activityToGoTo){
        Intent activityIntent = new Intent(this,activityToGoTo);
        startActivity(activityIntent);
    }

}
