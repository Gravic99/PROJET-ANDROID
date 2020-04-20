package com.example.qwikpik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CameraActivity extends AppCompatActivity {
    Button backToMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        backToMenuButton = findViewById(R.id.btn_Back);
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
}
