package com.example.qwikpik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    Button backToMenuButton;

    RecyclerView recyclerView;

    List<Picture> pictureList;
    GalleryRecyclerView GalleryRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        recyclerView = findViewById(R.id.RecyclerView_Gallery);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        pictureList = getData();
        GalleryRecyclerView = new GalleryRecyclerView(pictureList);
        recyclerView.setAdapter(GalleryRecyclerView);
        //recyclerView.invalidate();

        backToMenuButton = findViewById(R.id.btn_Back2);
        setListener();
    }

    private void setListener() {
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(Menu.class);
            }
        });
    }

    private void goToActivity(Class activityToGoTo){
        Intent activityIntent = new Intent(this,activityToGoTo);
        startActivity(activityIntent);
    }
    private ArrayList getData() {
        final ArrayList imageItems = new ArrayList();

        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] imageFiles = path.listFiles();
        for (int i = 0; i < imageFiles.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFiles[i].getAbsolutePath());
            imageItems.add(new Picture(bitmap, imageFiles[i].getName(),
                                        imageFiles[i].getAbsolutePath(),getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
        }

        return imageItems;}
}
