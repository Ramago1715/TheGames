package com.example.games;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.games.BD.GameDB;

public class Splash extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 5000;
    private ImageView imageView;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.cookieicon);
        textView = findViewById(R.id.cookietext);


        Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        imageView.startAnimation(slideUpAnimation);


        Animation slideUpAnimationForText = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        textView.startAnimation(slideUpAnimationForText);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, InicioSesion.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}
