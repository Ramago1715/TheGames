package com.example.games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.games.BD.GameDB;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InicioSesion extends AppCompatActivity {
    GameDB db;
    private ExecutorService executor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        this.db = GameDB.getInstance(getApplicationContext());
        this.executor = Executors.newSingleThreadExecutor();

    }

    public void iniciosesion(View view){
        if (){
            Intent inicio = new Intent(this, MainActivity.class);
            inicio.putExtra("email",gmail);
            inicio.putExtra("password",pass);
            startActivity(inicio);


        }

    }
    private boolean checkCorrectLogin(){

    }
    public void register(View view){
        Intent register = new Intent(this, Register.class);
        startActivity(register);
    }
}