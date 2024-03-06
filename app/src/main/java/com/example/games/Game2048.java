package com.example.games;

import static com.example.games.R.*;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.example.games.BD.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Game2048 extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private Button[][] tablero;
    private Button[][] tableroresplado;
    private Boolean respaldo;
    private String score;
    private GestureDetector gestureDetector;
    private boolean finalizarPartida;
    private CountDownTimer timer;
    int minuts = 5;
    int seconds = 30;

    int numColumns = 4;
    int numRows = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_game2048);
        this.respaldo = false;
        this.score = "";
        this.finalizarPartida = false;
        this.gestureDetector = new GestureDetector(this, this);
        this.tableroresplado = new Button[numRows][numColumns];
        this.tablero = new Button[numRows][numColumns];
        initLayout();
        initTimer();
        generarNuevoNumero();
        generarNuevoNumero();
    }

    private void initLayout() {
        GridLayout gridLayout = findViewById(id.GridLayout);
        gridLayout.setColumnCount(numColumns);
        gridLayout.setRowCount(numRows);
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                this.tablero[row][col] = creatingButtons(row, col, gridLayout);
            }
        }
    }

    private void initTimer() {
        int tiempo = this.minuts * 60 + this.seconds;
        this.timer = new CountDownTimer((tiempo) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (seconds == 0) {
                    minuts--;

                    seconds = 59;
                } else {
                    seconds--;
                }
                TextView time = findViewById(id.Timer);
                time.setText(String.format("%02d:%02d", minuts, seconds));
                if((minuts*60+seconds)*1000<(tiempo*1000)/2){
                    time.setTextColor(getResources().getColor(color.naranjaPeligro));
                }
                if((minuts*60+seconds)*1000<(tiempo*1000)/3 ) {
                    time.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFinish() {
                finalizarPartida(true);
            }
        };
        timer.start();
    }

    private void colorearBotones(int x, int y) {
        String casilla = String.valueOf(this.tablero[x][y].getText());
        int valor_casilla = Integer.parseInt(casilla);
        switch (valor_casilla) {
            case 2:
                this.tablero[x][y].setBackgroundTintList(getResources().getColorStateList(color.N2));
                break;
            case 4:
                this.tablero[x][y].setBackgroundTintList(getResources().getColorStateList(color.N4));
                break;
            case 8:
                this.tablero[x][y].setBackgroundTintList(getResources().getColorStateList(color.N8));
                break;
            case 16:
                this.tablero[x][y].setBackgroundTintList(getResources().getColorStateList(color.N16));
                break;
            case 32:
                this.tablero[x][y].setBackgroundTintList(getResources().getColorStateList(color.N32));
                break;
            case 64:
                this.tablero[x][y].setBackgroundTintList(getResources().getColorStateList(color.N64));
                break;
            case 128:
                this.tablero[x][y].setBackgroundTintList(getResources().getColorStateList(color.N128));
                break;
            case 256:
                this.tablero[x][y].setBackgroundTintList(getResources().getColorStateList(color.N256));
                break;
            case 512:
                this.tablero[x][y].setBackgroundTintList(getResources().getColorStateList(color.N512));
                break;
            case 1024:
                this.tablero[x][y].setBackgroundTintList(getResources().getColorStateList(color.N1024));
                break;
            case 2048:
                this.tablero[x][y].setBackgroundTintList(getResources().getColorStateList(color.N2048));
                break;
            default:
                this.tablero[x][y].setBackgroundTintList(getResources().getColorStateList(color.button_background_tint));
        }

    }

    private void upMoviment() {
        boolean movimeinto = true;
        generarRespaldo();
        while (movimeinto == true) {
            movimeinto = false;
            for (int x = 0; x <= this.tablero.length - 2; x++) {
                for (int y = 0; y <= this.tablero.length - 1; y++) {
                    if (this.tablero[x][y].getText().equals("") && !this.tablero[x + 1][y].getText().equals("")) {
                        movimeinto = true;
                        this.tablero[x][y].setText(this.tablero[x + 1][y].getText());
                        colorearBotones(x, y);
                        this.tablero[x + 1][y].setText("");
                        this.tablero[x + 1][y].setBackgroundTintList(getResources().getColorStateList(color.button_background_tint));

                    } else if (this.tablero[x + 1][y].getText().equals(this.tablero[x][y].getText()) && !this.tablero[x + 1][y].getText().equals("")) {
                        String casilla = String.valueOf(this.tablero[x + 1][y].getText());
                        int valor_casilla = Integer.parseInt(casilla) * 2;
                        this.tablero[x][y].setText(String.valueOf(valor_casilla));
                        colorearBotones(x, y);
                        this.tablero[x + 1][y].setText("");
                        this.tablero[x + 1][y].setBackgroundTintList(getResources().getColorStateList(color.button_background_tint));
                        sumarPuntuacion();
                        comprobarVictoria(valor_casilla);
                    }
                }
            }
        }

    }

    private void downMoviment() {
        boolean movimiento = true;
        generarRespaldo();
        while (movimiento == true) {
            movimiento = false;
            for (int x = this.tablero.length - 1; x >= 1; x--) {
                for (int y = 0; y <= this.tablero.length - 1; y++) {
                    if (this.tablero[x][y].getText().equals("") && !this.tablero[x - 1][y].getText().equals("")) {
                        movimiento = true;
                        this.tablero[x][y].setText(this.tablero[x - 1][y].getText());
                        colorearBotones(x, y);
                        this.tablero[x - 1][y].setText("");
                        this.tablero[x - 1][y].setBackgroundTintList(getResources().getColorStateList(color.button_background_tint));


                    } else if (this.tablero[x - 1][y].getText().equals(this.tablero[x][y].getText()) && !this.tablero[x - 1][y].getText().equals("")) {
                        String casilla = String.valueOf(this.tablero[x - 1][y].getText());
                        int valor_casilla = Integer.parseInt(casilla) * 2;
                        this.tablero[x][y].setText(String.valueOf(valor_casilla));
                        colorearBotones(x, y);
                        this.tablero[x - 1][y].setText("");
                        this.tablero[x - 1][y].setBackgroundTintList(getResources().getColorStateList(color.button_background_tint));
                        sumarPuntuacion();
                        comprobarVictoria(valor_casilla);
                    }
                }
            }
        }

    }

    private void leftMoviment() {
        boolean movimiento = true;
        generarRespaldo();
        while (movimiento == true) {
            movimiento = false;
            for (int x = 0; x <= this.tablero.length - 1; x++) {
                for (int y = 0; y <= this.tablero.length - 2; y++) {
                    if (this.tablero[x][y].getText().equals("") && !this.tablero[x][y + 1].getText().equals("")) {
                        movimiento = true;
                        this.tablero[x][y].setText(this.tablero[x][y + 1].getText());
                        colorearBotones(x, y);
                        this.tablero[x][y + 1].setText("");
                        this.tablero[x][y + 1].setBackgroundTintList(getResources().getColorStateList(color.button_background_tint));
                    } else if (this.tablero[x][y + 1].getText().equals(this.tablero[x][y].getText()) && !this.tablero[x][y + 1].getText().equals("")) {
                        String casilla = String.valueOf(this.tablero[x][y + 1].getText());
                        int valor_casilla = Integer.parseInt(casilla) * 2;
                        this.tablero[x][y].setText(String.valueOf(valor_casilla));
                        colorearBotones(x, y);
                        this.tablero[x][y + 1].setText("");
                        this.tablero[x][y + 1].setBackgroundTintList(getResources().getColorStateList(color.button_background_tint));
                        sumarPuntuacion();
                        comprobarVictoria(valor_casilla);
                    }
                }
            }
        }

    }

    private void rigthMoviment() {
        boolean movimiento = true;
        generarRespaldo();
        while (movimiento == true) {
            movimiento = false;
            for (int x = 0; x <= this.tablero.length - 1; x++) {
                for (int y = this.tablero.length - 1; y >= 1; y--) {
                    if (this.tablero[x][y].getText().equals("") && !this.tablero[x][y - 1].getText().equals("")) {
                        movimiento = true;
                        this.tablero[x][y].setText(this.tablero[x][y - 1].getText());
                        colorearBotones(x, y);
                        this.tablero[x][y - 1].setText("");
                        this.tablero[x][y - 1].setBackgroundTintList(getResources().getColorStateList(color.button_background_tint));
                    } else if (this.tablero[x][y - 1].getText().equals(this.tablero[x][y].getText()) && !this.tablero[x][y - 1].getText().equals("")) {
                        String casilla = String.valueOf(this.tablero[x][y - 1].getText());
                        int valor_casilla = Integer.parseInt(casilla) * 2;
                        this.tablero[x][y].setText(String.valueOf(valor_casilla));
                        colorearBotones(x, y);
                        this.tablero[x][y - 1].setText("");
                        this.tablero[x][y - 1].setBackgroundTintList(getResources().getColorStateList(color.button_background_tint));
                        sumarPuntuacion();
                        comprobarVictoria(valor_casilla);
                    }
                }
            }
        }

    }

    private void generarRespaldo() {
        FloatingActionButton rewind = findViewById(id.rewind);
        rewind.setBackgroundTintList(getResources().getColorStateList(color.botones));
        TextView textView = findViewById(id.Score);
        this.score = String.valueOf(textView.getText());
        for (int i = 0; i < this.tablero.length; i++) {
            for (int j = 0; j < this.tablero[i].length; j++) {
                Button botonOriginal = this.tablero[i][j];
                Button botonCopia = new Button(this);
                botonCopia.setText(botonOriginal.getText());
                botonCopia.setBackgroundTintList(botonOriginal.getBackgroundTintList());
                this.tableroresplado[i][j] = botonCopia;
            }
        }
        this.respaldo = true;

    }

    public void rewindMovimiento(View view) {
        FloatingActionButton rewind = findViewById(id.rewind);
        if (respaldo) {
            for (int i = 0; i < this.tableroresplado.length; i++) {
                for (int j = 0; j < this.tableroresplado[i].length; j++) {
                    Button botonRespaldo = this.tableroresplado[i][j];
                    Button botonOriginal = new Button(this);
                    botonOriginal.setText(botonRespaldo.getText());
                    botonOriginal.setBackgroundTintList(botonRespaldo.getBackgroundTintList());
                    this.tablero[i][j].setText(botonOriginal.getText());
                    this.tablero[i][j].setBackgroundTintList(botonOriginal.getBackgroundTintList());
                }
            }
            respaldo = false;
            rewind.setBackgroundTintList(getResources().getColorStateList(color.derrota));
            TextView textView = findViewById(id.Score);
            textView.setText(this.score);
        } else {
            Toast.makeText(this, "No puedes rebobinar", Toast.LENGTH_SHORT).show();
        }
    }

    private void comprobarVictoria(int valor_casilla) {
        if (valor_casilla == 2024) {
            finalizarPartida(false);
        }
    }

    private boolean comprobarDerrota() {
        boolean derrota = true;
        int x = 0;
        while (x <= this.tablero.length - 1 && derrota == true) {
            for (int y = 0; y <= this.tablero.length - 1; y++) {
                if (this.tablero[x][y].getText().equals("")) {
                    derrota = false;
                }

            }
            x++;
        }
        return derrota;

    }

    private void finalizarPartida(boolean derrota) {
        this.finalizarPartida = true;
        Button you = this.tablero[1][1];
        Button DV = this.tablero[1][2];
        if (derrota) {
            you.setText("YOU");
            you.setBackgroundTintList(getResources().getColorStateList(color.derrota));
            DV.setText("LOSE!");
            DV.setBackgroundTintList(getResources().getColorStateList(color.derrota));
            ImageButton rewind = findViewById(id.rewind);
            rewind.setClickable(false);
            rewind.setBackgroundTintList(getResources().getColorStateList(color.derrota));
            timer.cancel();
        } else {
            you.setText("YOU");
            you.setBackgroundTintList(getResources().getColorStateList(color.victoria));
            DV.setText("WIN!!!!");
            DV.setBackgroundTintList(getResources().getColorStateList(color.victoria));
            timer.cancel();
        }

    }

    private void generarNuevoNumero() {
        boolean asignado = false;
        int contador = 0;
        while (!asignado) {
            contador++;
            int x = (int) (Math.random() * 4);
            int y = (int) (Math.random() * 4);
            if (this.tablero[x][y].getText().equals("")) {
                this.tablero[x][y].setText("2");
                this.tablero[x][y].setBackgroundTintList(getResources().getColorStateList(color.N2));
                asignado = true;

            } else if (contador > 4) {
                if (comprobarDerrota()) {
                    finalizarPartida(true);
                    asignado = true;
                } else {
                    contador = 0;
                }
            }
        }

    }

    private Button creatingButtons(int row, int col, GridLayout gridLayout) {
        Button button = new Button(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();

        params.width = GridLayout.LayoutParams.WRAP_CONTENT;
        params.height = 250;
        params.columnSpec = GridLayout.spec(col, 1f);
        params.rowSpec = GridLayout.spec(row, 1f);
        params.setGravity(Gravity.FILL);
        params.setGravity(Gravity.CENTER);

        button.setBackgroundTintList(getResources().getColorStateList(color.button_background_tint));
        button.setTextColor(getResources().getColor(color.button_text_color));
        button.setTextColor(Color.WHITE);
        button.setTextSize(25);
        button.setClickable(false);


        button.setLayoutParams(params);
        gridLayout.addView(button);

        return button;
    }

    public void backtoHub2048(View view) {
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);

    }

    public void nuevaPartida(View view) {
        Intent intent = new Intent(this, this.getClass());
        this.finish();
        this.startActivity(intent);


    }

    private void sumarPuntuacion() {
        TextView textView = findViewById(id.Score);
        String score = String.valueOf(textView.getText());
        int puntuacion = Integer.parseInt(score) + 1;
        textView.setText(String.valueOf(puntuacion));
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (!this.finalizarPartida) {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {

                if (velocityX > 0) {
                    rigthMoviment();
                    generarNuevoNumero();
                } else {
                    leftMoviment();
                    generarNuevoNumero();
                }
            } else {
                if (velocityY > 0) {
                    downMoviment();
                    generarNuevoNumero();
                } else {
                    upMoviment();
                    generarNuevoNumero();
                }
            }
        }
        return true;
    }
}

