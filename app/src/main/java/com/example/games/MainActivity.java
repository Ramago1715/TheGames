package com.example.games;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.games.BD.GameDB;
import com.example.games.BD.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.db = GameDB.getInstance(getApplicationContext());
        this.executor = Executors.newSingleThreadExecutor();

        Intent intent = getIntent();
        this.usuario = (Usuario) intent.getSerializableExtra("usuario");
        setImageProfile(getImageprofile(this.usuario.getEmail()));

        TextView textView = findViewById(R.id.userNameMainActivity);
        if (this.usuario != null) {
            textView.setText(this.usuario.getUser());

        } else {
            textView.setText("Usuario no encontrado");
        }

        inicializarRecycledView(this.usuario);
    }

    private byte[] getImageprofile(String email) {
        Future<byte[]> future = executor.submit(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return db.SettingsDAO().getFotoPerfilById(email);
            }
        });
        return null;
    }



    @Override
    protected void onResume() {
        if (executor == null || executor.isShutdown()) {
            executor = Executors.newSingleThreadExecutor();
        }

        super.onResume();
    }

    private void inicializarRecycledView(Usuario usuario) {
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

    public void openGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onPause() {
        super.onPause();
        updateUserData();
    }

    private void updateUserData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (usuario != null) {
                    db.SettingsDAO().Update(usuario);
                    Log.d("PRUEBA", "Datos de usuario actualizados en la base de datos");
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                setImageProfile(byteArray);
                this.usuario.setFotoPerfil(byteArray);
                Log.d("PRUEBA", "Antes de ejecutar el Runnable");
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (usuario != null) {
                            Log.d("PRUEBA", "Usuario y DAO no son nulos");
                            db.SettingsDAO().Update(usuario);
                        } else {
                            Log.d("PRUEBA", "Usuario o DAO son nulos");
                        }
                    }
                });

                Toast.makeText(this, "Imagen seleccionada correctamente", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.d("PRUEBA", "Error al ejecutar la actualización en la base de datos", e);
            }
        }
    }
    private void setImageProfile(byte[] byteArray){
        ImageView profilefoto = findViewById(R.id.fotoperfil);
        if (byteArray != null && byteArray.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            if (bitmap != null) {
                profilefoto.setImageBitmap(bitmap);
            } else {
                Log.d("PRUEBA", "Error al decodificar el bitmap");
                profilefoto.setImageResource(R.mipmap.fotodeperfil); // Cargar una imagen predeterminada en caso de error
            }
        } else {
            Log.d("PRUEBA", "ByteArray nulo o vacío");
            profilefoto.setImageResource(R.mipmap.fotodeperfil); // Cargar una imagen predeterminada si el byteArray es nulo o vacío
        }
    }
}
