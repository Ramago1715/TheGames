package com.example.games;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public int puntuacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void go2048(View view){
        Intent go2048 = new Intent(this, game_2048.class);
        startActivity(go2048);
    }
    public void gosenku(View view){
        Intent gosenku = new Intent(this, senku.class);
        startActivity(gosenku);
    }
}