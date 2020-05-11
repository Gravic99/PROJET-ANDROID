package com.example.qwikpik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Button backToMenuButton;
    Button btn_TakePicture;
    ImageView ImageView_Picture;
    String pathToFile;
    String photoPath;
    Location location;
    TextView textViewName;
    double longitude = 46.1262094;//location.getLongitude();
    double latitude = -70.363952;//location.getLatitude();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        backToMenuButton = findViewById(R.id.btn_Back);
        btn_TakePicture = findViewById(R.id.btn_TakePicture);
        ImageView_Picture = findViewById(R.id.imageView_Picture);
        textViewName = findViewById(R.id.textViewName);

        requestPermission();
        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
            if(requestCode == 1){
                createAndShowNameDialog();
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                ImageView_Picture.setImageBitmap(bitmap);
            }
        }
        else{
             Toast.makeText(this,"no", Toast.LENGTH_SHORT).show();
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
        String name = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File StorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name,".jpeg",StorageDir);
            photoPath= image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

    return image;

    }
    private void createAndShowNameDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.name_dialog);

        Button dialogBtnSave = dialog.findViewById(R.id.btnSave);
        Button dialogBtnCancel = dialog.findViewById(R.id.btnCancel);

        dialogBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextName = dialog.findViewById(R.id.editTextName);
                textViewName.setText(editTextName.getText());
                renamePicture(editTextName.getText().toString());
                dialog.dismiss();
            }
        });

        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewName.setText("");
                renamePicture("");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void renamePicture(String newName){
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File from      = new File(photoPath);
        File to        = new File(directory, newName.trim() +
                                "_lat:" + Double.toString(latitude) + ",lon:" +
                                Double.toString(longitude) + ",date:" + date + ".jpeg");
        from.renameTo(to);
    }

}
