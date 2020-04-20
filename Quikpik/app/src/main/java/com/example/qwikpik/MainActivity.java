package com.example.qwikpik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartUpAnimation();

    }

    private void StartUpAnimation() {
        imageView = findViewById(R.id.imageView);
        if(imageView == null) throw new AssertionError();
        imageView.setBackgroundResource(R.drawable.animation_startup);
        anim = (AnimationDrawable)imageView.getBackground();
        Handler handlerAnimSwitch = new Handler();
        final Intent intentMenu = new Intent(this, Menu.class);
        anim.start();
        handlerAnimSwitch.postDelayed(new Runnable() {
            @Override
            public void run() {
                anim.stop();
                startActivity(intentMenu);
            }
        },4200);
    }
}
