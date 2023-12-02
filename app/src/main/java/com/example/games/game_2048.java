package com.example.games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class game_2048 extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private Button[][] matrizbotones;
    private GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048);
        GridLayout gridLayout = findViewById(R.id.GridLayout);
        int numColumns = 4;
        int numRows = 4;
        gestureDetector = new GestureDetector(this, this);
        matrizbotones = new Button[4][4];

        gridLayout.setColumnCount(numColumns);
        gridLayout.setRowCount(numRows);
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                this.matrizbotones[row][col] = CreatingButtons(row, col, gridLayout);
            }
        }
        GenerarNuevoNumero();
        GenerarNuevoNumero();


    }
    private void UpMoviment(){
        for (int x=0;x<=this.matrizbotones.length-2;x++){
            for (int y=0;y<=this.matrizbotones.length-1;y++){
                if(this.matrizbotones[x][y].getText().equals("")  && !this.matrizbotones[x+1][y].getText().equals("")) {
                    this.matrizbotones[x][y].setText(this.matrizbotones[x + 1][y].getText());
                    this.matrizbotones[x + 1][y].setText("");
                    SumarPuntuacion();
                }
            }
        }
    }

    private void DownMoviment(){
        for (int x=this.matrizbotones.length-1;x>=1;x--){
            for (int y=0;y<=this.matrizbotones.length-1;y++){
                if(this.matrizbotones[x][y].getText().equals("") && !this.matrizbotones[x-1][y].getText().equals("")){
                    this.matrizbotones[x][y].setText(this.matrizbotones[x - 1][y].getText());
                    this.matrizbotones[x - 1][y].setText("");
                    SumarPuntuacion();

                }
            }
        }
    }

    private void LeftMoviment(){
        for (int x=0;x<=this.matrizbotones.length-1;x++){
            for (int y=0;y<=this.matrizbotones.length-2;y++){
                if(this.matrizbotones[x][y].getText().equals("")  && !this.matrizbotones[x][y+1].getText().equals("")) {
                    this.matrizbotones[x][y].setText(this.matrizbotones[x][y+1].getText());
                    this.matrizbotones[x][y+1].setText("");
                    SumarPuntuacion();
                }
            }
        }
    }


    private void RigthMoviment(){
        for (int x=0;x<=this.matrizbotones.length-1;x++){
            for (int y=this.matrizbotones.length-1;y>=1;y--){
                if(this.matrizbotones[x][y].getText().equals("")  && !this.matrizbotones[x][y-1].getText().equals("")) {
                    this.matrizbotones[x][y].setText(this.matrizbotones[x][y-1].getText());
                    this.matrizbotones[x][y-1].setText("");
                    SumarPuntuacion();
                }
            }
        }
    }

    private boolean ComprobarDerrota() {
        boolean derrota = true;
        int x = 0;
        while (x <= this.matrizbotones.length - 1 && derrota == true) {
            for (int y = 0; y <= this.matrizbotones.length - 1; y++) {
                if (this.matrizbotones[x][y].getText().equals("")) {
                    derrota = false;
                }

            }
            x++;
        }
        return derrota;

    }

    private void GenerarNuevoNumero() {
        boolean asignado = false;
        int contador = 0;
        while (!asignado) {
            contador++;
            int x = (int) (Math.random() * 4);
            int y = (int) (Math.random() * 4);
            if (this.matrizbotones[x][y].getText().equals("")) {
                this.matrizbotones[x][y].setText("2");
                asignado = true;

            } else if (contador > 7) {
                if (ComprobarDerrota()) {
                    Toast.makeText(this,"HAS PERDIDO PRINGADO",Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Intent intent = new Intent(this, this.getClass());
                    asignado = true;
                    this.finish();
                    this.startActivity(intent);
                } else {
                    contador = 0;
                }
            }
        }

    }

    private Button CreatingButtons(int row, int col, GridLayout gridLayout) {
        Button button = new Button(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();

        params.width = GridLayout.LayoutParams.WRAP_CONTENT;
        params.height = 250;
        params.columnSpec = GridLayout.spec(col, 1f);
        params.rowSpec = GridLayout.spec(row, 1f);
        params.setGravity(Gravity.FILL);
        params.setGravity(Gravity.CENTER);

        button.setBackgroundTintList(getResources().getColorStateList(R.color.button_background_tint));
        button.setTextColor(getResources().getColor(R.color.button_text_color));
        button.setTextColor(Color.WHITE);
        button.setTextSize(25);


        button.setLayoutParams(params);
        gridLayout.addView(button);

        return button;
    }

    public void BacktoHub2048(View view) {
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);

    }

    public void NuevaPartida(View view) {
        Intent intent = new Intent(this, this.getClass());
        this.finish();
        this.startActivity(intent);

    }

    private void SumarPuntuacion(){
        TextView textView = findViewById(R.id.button20);
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
        if (Math.abs(velocityX) > Math.abs(velocityY)) {

            if (velocityX > 0) {
                RigthMoviment();

                GenerarNuevoNumero();
            } else {
                LeftMoviment();

                GenerarNuevoNumero();
            }
        } else {
            if (velocityY > 0) {
                DownMoviment();
                GenerarNuevoNumero();


            } else {
                UpMoviment();
                GenerarNuevoNumero();



            }
        }
        return true;
    }

}

