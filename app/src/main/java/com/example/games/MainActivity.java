package com.example.games;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public int puntuacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        System.currentTimeMillis();
        super.onResume();
    }

    public void go2048(View view){
        Intent go2048 = new Intent(this, Game2048.class);
        startActivity(go2048);
    }
    public void goSenku(View view){
        Intent gosenku = new Intent(this, Senku.class);
        startActivity(gosenku);
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }
}