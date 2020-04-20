package com.example.qwikpik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {
    Button goToGalleryButton;
    Button goToCameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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

        goToCameraButton = findViewById(R.id.btn_TakeAPik);
        goToGalleryButton = findViewById(R.id.btn_Gallery);
        setListener();
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
    }

    private void goToActivity(Class activityToGoTo){
        Intent activityIntent = new Intent(this,activityToGoTo);
        startActivity(activityIntent);
    }
}
