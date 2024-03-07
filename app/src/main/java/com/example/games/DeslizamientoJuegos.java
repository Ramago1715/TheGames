package com.example.games;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.games.BD.GameDB;
import com.example.games.BD.Usuario;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DeslizamientoJuegos extends ItemTouchHelper.SimpleCallback {
    GameDB db;
    private ExecutorService executor;
    private AdaptadorJuego mAdapter;
    private Context mContext;
    private Usuario usuario;

    public DeslizamientoJuegos(Context context, AdaptadorJuego adapter,Usuario usuario) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        mContext = context;
        this.usuario = usuario;
        this.db = GameDB.getInstance(mContext);
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        switch (direction) {
            case ItemTouchHelper.LEFT:
            case ItemTouchHelper.RIGHT:

                Intent leftIntent;
                switch (position) {
                    case 0:
                        mContext.startActivity(guardarDatosDeSession(new Senku()));
                        break;
                    case 1:
                        mContext.startActivity(guardarDatosDeSession(new Game2048()));
                        break;

                    default:

                }
                mAdapter.notifyItemChanged(position);
                break;
        }
    }
    private Intent guardarDatosDeSession(AppCompatActivity activity){
        Intent inicio = new Intent(mContext, activity.getClass());
        inicio.putExtra("usuario",this.usuario);
        return inicio;

    }
    }
