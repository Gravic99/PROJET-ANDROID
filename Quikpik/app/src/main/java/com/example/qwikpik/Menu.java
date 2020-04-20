package com.example.qwikpik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Menu extends AppCompatActivity {

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
    }
}
