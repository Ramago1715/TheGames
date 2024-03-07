package com.example.games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.games.BD.GameDB;
import com.example.games.BD.Usuario;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        EditText email = findViewById(R.id.EmailLogin);
        EditText password = findViewById(R.id.PasswordLogin);
        String emailtext = String.valueOf(email.getText());
        String passwordtext = String.valueOf(password.getText());

        if (checkCorrectLogin(emailtext,passwordtext)){
            startActivity(guardarDatosDeSession(emailtext,passwordtext));

        }else{
            TextView errormsg = findViewById(R.id.ErrorText);
            errormsg.setVisibility(View.VISIBLE);
            errormsg.setText("Credenciales Incorrectas");
            password.setText("");
        }

    }
    private boolean checkCorrectLogin(String email,String password){
        Future<Boolean> future = executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return db.LogInDAO().existUser(email, password);
            }
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void register(View view){
        Intent register = new Intent(this, Register.class);
        startActivity(register);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    private Intent guardarDatosDeSession(String emailtext,String passwordtext){
        Intent inicio = new Intent(this, MainActivity.class);
        Future<Usuario> future = executor.submit(new Callable<Usuario>() {
            @Override
            public Usuario call() throws Exception {
                return db.LogInDAO().login(emailtext,passwordtext);
            }
        });

        try {
            Usuario usuario = future.get();
            inicio.putExtra("usuario",usuario);
            return inicio;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}