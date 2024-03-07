package com.example.games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.games.BD.GameDB;
import com.example.games.BD.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    public int puntuacion;
    private RecyclerView recyclerView;
    private AdaptadorJuego adapter;
    private Usuario usuario;
    GameDB db;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate()");

        this.db = GameDB.getInstance(getApplicationContext());
        this.executor = Executors.newSingleThreadExecutor();

        Intent intent = getIntent();
        this.usuario = (Usuario) intent.getSerializableExtra("usuario");


        TextView textView = findViewById(R.id.userNameMainActivity);
        if (this.usuario != null) {
            textView.setText(this.usuario.getUser());
        } else {
            textView.setText("Usuario no encontrado");
        }

        inicializarRecycledView(this.usuario);
    }

    @Override
    protected void onPause() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        Log.d("MainActivity", "onPause()");
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (executor == null || executor.isShutdown()) {
            executor = Executors.newSingleThreadExecutor();
        }
        Log.d("MainActivity", "onResume()");
        super.onResume();
    }

    private void inicializarRecycledView(Usuario usuario) {
        Log.d("MainActivity", "inicializarRecycledView()");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> dataList = new ArrayList<>();
        List<String> descripcion = new ArrayList<>();
        List<Integer> imagelist = new ArrayList<>();
        dataList.add("SENKU");
        descripcion.add("Este juego de ingenio comienza con un tablero lleno de piezas, salvo un hueco. El objetivo es eliminar todas las piezas del tablero excepto una." +
                " Para eliminar una ficha, mueve una de las piezas en horizontal o vertical saltando sobre una ficha contigua y acabando en el hueco junto a la pieza saltada que será eliminada.");
        imagelist.add(R.mipmap.senkulogo);
        dataList.add("2048");
        descripcion.add("El objetivo del juego es desplazar fichas numeradas en una cuadrícula para combinarlas y crear una ficha con el número 2048. " +
                "El juego se juega en una cuadrícula de 4x4 y las fichas numeradas comienzan con dos 2s. El juego termina cuando la cuadrícula está llena y no quedan más movimientos.");
        imagelist.add(R.mipmap.a2048logo);

        adapter = new AdaptadorJuego(dataList, descripcion, imagelist);
        recyclerView.setAdapter(adapter);
        DeslizamientoJuegos deslizamientoJuegos = new DeslizamientoJuegos(this, adapter,usuario);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(deslizamientoJuegos);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setUser(String email, String password) {
        Future<Usuario> future = executor.submit(new Callable<Usuario>() {
            @Override
            public Usuario call() throws Exception {
                return db.LogInDAO().login(email, password);
            }
        });

        try {
            this.usuario = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
