package com.example.games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    GameDB db;
    private ExecutorService executor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.db = GameDB.getInstance(getApplicationContext());
        this.executor = Executors.newSingleThreadExecutor();

    }
    public void registro(View view){
        if(validDataEntry()){
            login(view);
        }
    }
    private boolean validDataEntry(){
        TextView errormsg = findViewById(R.id.ErrorText);
        errormsg.setText("");
        EditText username = findViewById(R.id.UserName);
        EditText email = findViewById(R.id.Email);
        EditText password = findViewById(R.id.Password);
        EditText confirmPassword = findViewById(R.id.ConfirmPassword);

        if(!FilledFields(String.valueOf(username.getText()),String.valueOf(email.getText()),String.valueOf(password.getText()),String.valueOf(confirmPassword.getText()))){
            Log.d("PRUEBA", "1");
            errormsg.setVisibility(View.VISIBLE);
            errormsg.setText("Tienes que rellenar todos los campos");
            return false;

        }else if (!isValidEmail(String.valueOf(email.getText()))) {
            errormsg.setVisibility(View.VISIBLE);
            Log.d("PRUEBA", "2");
            errormsg.setText("El correo que has escrito no es valido");
            email.setText("");
            return false;

        }else if (emailAlreadyExist(String.valueOf(email.getText()))) {
            Log.d("PRUEBA", "3");
            errormsg.setVisibility(View.VISIBLE);
            errormsg.setText("El correo ya existe");
            email.setText("");
            return false;
        } else if (!String.valueOf(password.getText()).equals(String.valueOf(confirmPassword.getText()))) {
            Log.d("PRUEBA", "4");
            errormsg.setVisibility(View.VISIBLE);
            errormsg.setText("Las contraseñas son diferentes");
            password.setText("");
            confirmPassword.setText("");
            return false;
        } else{
            Usuario usuario = new Usuario();
            usuario.setEmail(String.valueOf(email.getText()));
            usuario.setUser(String.valueOf(username.getText()));
            usuario.setPassword(String.valueOf(password.getText()));
            usuario.setFotoPerfil(null);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    db.RegisterDAO().insert(usuario);
                }
            });
            return true;
        }
    }
    private boolean emailAlreadyExist(String email) {
        Future<Boolean> future = executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return db.RegisterDAO().alreadyExist(email);
            }
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }


    private boolean FilledFields(String email,String user,String password,String confirmpassword){
        if (email.equals("") && user.equals("") && password.equals("") && confirmpassword.equals("")){
            return false;
        }else{
            return true;
        }
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public void login(View view){
        Intent login = new Intent(this, InicioSesion.class);
        startActivity(login);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}