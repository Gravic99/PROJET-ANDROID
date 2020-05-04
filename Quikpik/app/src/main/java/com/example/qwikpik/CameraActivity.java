package com.example.qwikpik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    Button backToMenuButton;
    Button btn_TakePicture;
    ImageView ImageView_Picture;
    String pathToFile;
    String photoPath;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        backToMenuButton = findViewById(R.id.btn_Back);
        btn_TakePicture = findViewById(R.id.btn_TakePicture);
        ImageView_Picture = findViewById(R.id.imageView_Picture);
        requestPermission();
        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this,Integer.toString(requestCode), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,Integer.toString(resultCode), Toast.LENGTH_SHORT).show();

        if(resultCode == RESULT_OK){

            if(requestCode == 1){
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                ImageView_Picture.setImageBitmap(bitmap);
            }
        }
    }

    private void requestPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }
    private void setListener() {
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(Menu.class);
            }
        });
        btn_TakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPictureTakeAction();
            }
        });
    }

    private void goToActivity(Class activityToGoTo){
        Intent activityIntent = new Intent(this,activityToGoTo);
        startActivity(activityIntent);
    }
    private void dispatchPictureTakeAction(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if(takePicture.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            photoFile = createPhotoFile();
            if(photoFile != null){
                pathToFile = photoFile.getAbsolutePath();
               // Toast.makeText(this,pathToFile, Toast.LENGTH_SHORT).show();
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.qwikpik.fileprovider",photoFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takePicture,1);


            }

        }
    }
    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File StorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
       // image.renameTo(image)

       // File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
        //File from      = new File(directory, "currentFileName");
        //File to        = new File(directory, text.trim() + ".mp4");
        //File test = new File(photoPath);
        //from.renameTo(to);
        //Toast.makeText(this,StorageDir.toString(), Toast.LENGTH_SHORT).show();
        try {
            image = File.createTempFile(name,".jpg",StorageDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
        photoPath= image.getAbsolutePath();
        //Toast.makeText(this,photoPath, Toast.LENGTH_SHORT).show();
        double longitude = 46.1262094;//location.getLongitude();
        double latitude = -70.363952;//location.getLatitude();

       Toast.makeText(this,Double.toString(longitude), Toast.LENGTH_SHORT).show();
       Toast.makeText(this,Double.toString(latitude), Toast.LENGTH_SHORT).show();
        //geoTag(photoPath, latitude, longitude);
    return image;

    }

    public void geoTag(String filename, double latitude, double longitude){
        ExifInterface exif;

        try {
            exif = new ExifInterface(filename);
            int num1Lat = (int)Math.floor(latitude);
            int num2Lat = (int)Math.floor((latitude - num1Lat) * 60);
            double num3Lat = (latitude - ((double)num1Lat+((double)num2Lat/60))) * 3600000;

            int num1Lon = (int)Math.floor(longitude);
            int num2Lon = (int)Math.floor((longitude - num1Lon) * 60);
            double num3Lon = (longitude - ((double)num1Lon+((double)num2Lon/60))) * 3600000;

            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, num1Lat+"/1,"+num2Lat+"/1,"+num3Lat+"/1000");
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, num1Lon+"/1,"+num2Lon+"/1,"+num3Lon+"/1000");


            if (latitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            }

            if (longitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            }

            exif.saveAttributes();

        } catch (IOException e) {
            Log.e("PictureActivity", e.getLocalizedMessage());
        }

    }

}
