package com.example.games;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.games.BD.GameDB;
import com.example.games.BD.Usuario;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Senku extends AppCompatActivity {
    private int minuts = 0;
    private int seconds = 3;
    private CountDownTimer timer;
    private Usuario usuario;
    public GridLayout gridLayout;
    private final ImageButton[][] ArrayImageButtons = new ImageButton[7][7];

    private TextView temporizador;

    private enum ButtonState {
        ON,
        OFF,
        SELECTED
    }

    private final ImageButton[][] backupImageButtons = new ImageButton[7][7];

    private ImageView imageBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.usuario = (Usuario) intent.getSerializableExtra("usuario");
        initTimer();
        temporizador = findViewById(R.id.txtTime);
        setContentView(R.layout.activity_senku);
        gridLayout = findViewById(R.id.buttonsContainer);
        lowerLayerButtons();
        createGameButtons();
        imageBack = findViewById(R.id.boton_paso_atras);
        imageBack.setOnClickListener(v -> {
            imageBack.setBackgroundTintList(getResources().getColorStateList(R.color.derrota));
            restoreArrayButtonsFromBackup();
            updateUI();
            imageBack.setClickable(false);

        });
    }

    private void createGameButtons() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                final int row = i;
                final int col = j;
                ImageButton imageButton = new ImageButton(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(j));

                int widthInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 49, getResources().getDisplayMetrics());
                int heightInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 47, getResources().getDisplayMetrics());
                params.width = widthInDp;
                params.height = heightInDp;

                imageButton.setLayoutParams(params);
                imageButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));

                if ((i == 0 || i == 1 || i == 5 || i == 6) && (j == 0 || j == 1 || j == 5 || j == 6)) {
                    imageButton.setImageDrawable(null);
                } else {
                    if (i == 3 && j == 3) {
                        imageButton.setImageResource(R.drawable.radio_button_off);
                        imageButton.setTag(ButtonState.OFF);
                    } else {
                        imageButton.setImageResource(R.drawable.radio_button_on);
                        imageButton.setTag(ButtonState.ON);
                    }
                }

                ArrayImageButtons[i][j] = imageButton;

                gridLayout.addView(imageButton);
                imageButton.setOnClickListener(v -> handleImageButtonClick(imageButton, row, col));
            }
        }



    }

    private void lowerLayerButtons() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                ImageButton imageButton = new ImageButton(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(j));

                int widthInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 49, getResources().getDisplayMetrics());
                int heightInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 47, getResources().getDisplayMetrics());
                params.width = widthInDp;
                params.height = heightInDp;

                imageButton.setLayoutParams(params);
                imageButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));

                if ((i == 0 || i == 1 || i == 5 || i == 6) && (j == 0 || j == 1 || j == 5 || j == 6)) {
                    // Configura las bolas OFF en la capa inferior
                    imageButton.setImageDrawable(null);
                } else {
                    // Configura las bolas OFF en la capa superior en lugar de ON
                    imageButton.setImageResource(R.drawable.radio_button_off);
                    imageButton.setTag(ButtonState.OFF);

                }

                gridLayout.addView(imageButton);
            }
        }
    }


    public void handleImageButtonClick(ImageButton clickedImageButton, int row, int col) {
        backupArrayButtons();
        if (!isAnyButtonSelected()) {
            if (ButtonState.ON.equals(clickedImageButton.getTag())) {
                clickedImageButton.setImageResource(R.drawable.radio_button_custom);
                clickedImageButton.setTag(ButtonState.SELECTED);
            }
        } else if (ButtonState.OFF.equals(clickedImageButton.getTag())) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    if (ButtonState.SELECTED.equals(ArrayImageButtons[i][j].getTag()) &&
                            isDistanceTwo(i, j, row, col)) {


                        ArrayImageButtons[i][j].setImageResource(R.drawable.radio_button_off);
                        ArrayImageButtons[i][j].setTag(ButtonState.OFF);

                        // Switch the ball between ON and SELECTED to OFF
                        switchBallsOff(clickedImageButton,i, j, row, col);



                    }
                }
            }
        } else if (ButtonState.SELECTED.equals(clickedImageButton.getTag())) {
            clickedImageButton.setImageResource(R.drawable.radio_button_on);
            clickedImageButton.setTag(ButtonState.ON);
        }
    }
    private void restaractivity(){
        this.finish();
        this.startActivity(guardarDatosDeSession(this));
    }

    private void handleGameWinned() {
        temporizador.setText("¡Has Ganado!");
        AlertDialog.Builder builder = new AlertDialog.Builder(Senku.this);
        builder.setTitle("¡Felicidades! Has Ganado")
                .setMessage("¿Quieres volver a jugar?")
                .setPositiveButton("Sí", (dialog, which) -> restaractivity())
                .setNegativeButton("No", (dialog, which) -> {
                    this.startActivity(guardarDatosDeSession(new MainActivity()));
                    dialog.dismiss();
                })
                .setCancelable(false);

        AlertDialog gameOverDialog = builder.create();
        gameOverDialog.show();
    }


    private void switchBallsOff(ImageButton clickedImageButton, int selectedRow, int selectedCol, int offRow, int offCol) {
        int middleRow = (selectedRow + offRow) / 2;
        int middleCol = (selectedCol + offCol) / 2;

        if (ButtonState.ON.equals(ArrayImageButtons[middleRow][middleCol].getTag())) {
            int startX = ArrayImageButtons[selectedRow][selectedCol].getLeft() - ArrayImageButtons[middleRow][middleCol].getLeft();
            int startY = ArrayImageButtons[selectedRow][selectedCol].getTop() - ArrayImageButtons[middleRow][middleCol].getTop();
            int endX = ArrayImageButtons[offRow][offCol].getLeft() - ArrayImageButtons[middleRow][middleCol].getLeft();
            int endY = ArrayImageButtons[offRow][offCol].getTop() - ArrayImageButtons[middleRow][middleCol].getTop();

            TranslateAnimation translateAnimation = new TranslateAnimation(startX, endX, startY, endY);
            translateAnimation.setDuration(500);


            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    ArrayImageButtons[middleRow][middleCol].setImageResource(R.drawable.radio_button_off);
                    ArrayImageButtons[middleRow][middleCol].setTag(ButtonState.OFF);


                    clickedImageButton.setImageResource(R.drawable.radio_button_on);
                    clickedImageButton.setTag(ButtonState.ON);


                    if (isGameWinned()) {
                        handleGameWinned();

                    } else if (isGameOver()) {
                        handleGameOver();

                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


            ArrayImageButtons[middleRow][middleCol].startAnimation(translateAnimation);
            imageBack.setClickable(true);
            imageBack.setBackgroundTintList(getResources().getColorStateList(R.color.botones));
        }
    }





    private boolean isDistanceTwo(int selectedRow, int selectedCol, int offRow, int offCol) {
        int middleRow = (selectedRow + offRow) / 2;
        int middleCol = (selectedCol + offCol) / 2;


        if (ButtonState.ON.equals(ArrayImageButtons[middleRow][middleCol].getTag())) {
            int rowDistance = Math.abs(selectedRow - offRow);
            int colDistance = Math.abs(selectedCol - offCol);
            return (rowDistance == 2 && colDistance == 0) || (rowDistance == 0 && colDistance == 2);
        }

        return false;
    }

    private boolean isGameOver() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (ButtonState.ON.equals(ArrayImageButtons[i][j].getTag())) {

                    if (i > 1 && ButtonState.ON.equals(ArrayImageButtons[i - 1][j].getTag()) && ButtonState.OFF.equals(ArrayImageButtons[i - 2][j].getTag())) {
                        return false;
                    }

                    if (i < 5 && ButtonState.ON.equals(ArrayImageButtons[i + 1][j].getTag()) && ButtonState.OFF.equals(ArrayImageButtons[i + 2][j].getTag())) {
                        return false;
                    }

                    if (j > 1 && ButtonState.ON.equals(ArrayImageButtons[i][j - 1].getTag()) && ButtonState.OFF.equals(ArrayImageButtons[i][j - 2].getTag())) {
                        return false;
                    }

                    if (j < 5 && ButtonState.ON.equals(ArrayImageButtons[i][j + 1].getTag()) && ButtonState.OFF.equals(ArrayImageButtons[i][j + 2].getTag())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }





    private boolean isGameWinned() {
        int count = 0;
        for (ImageButton[] row : ArrayImageButtons) {
            for (ImageButton button : row) {
                if (ButtonState.ON.equals(button.getTag())) {
                    count++;
                }
            }
        }
        return count == 1;
    }




    private boolean isAnyButtonSelected() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (ButtonState.SELECTED.equals(ArrayImageButtons[i][j].getTag())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void resetGame(View view) {
        this.timer.cancel();
        Intent intent = new Intent(this, this.getClass());
        this.finish();
        this.startActivity(intent);
    }



    private void backupArrayButtons() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                backupImageButtons[i][j] = new ImageButton(this);
                backupImageButtons[i][j].setImageDrawable(ArrayImageButtons[i][j].getDrawable());
                backupImageButtons[i][j].setTag(ArrayImageButtons[i][j].getTag());
                backupImageButtons[i][j].setClickable(ArrayImageButtons[i][j].isClickable());
            }
        }
    }

    private void restoreArrayButtonsFromBackup() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                ArrayImageButtons[i][j].setImageDrawable(backupImageButtons[i][j].getDrawable());
                ArrayImageButtons[i][j].setTag(backupImageButtons[i][j].getTag());
                ArrayImageButtons[i][j].setClickable(backupImageButtons[i][j].isClickable());
            }
        }
    }



    private void updateUI() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                ImageButton button = ArrayImageButtons[i][j];
                if ((i == 0 || i == 1 || i == 5 || i == 6) && (j == 0 || j == 1 || j == 5 || j == 6)) {
                    button.setImageDrawable(null);
                } else {
                    if (ButtonState.OFF.equals(button.getTag())) {
                        button.setImageResource(R.drawable.radio_button_off);
                    } else if (ButtonState.ON.equals(button.getTag())) {
                        button.setImageResource(R.drawable.radio_button_on);
                    } else if (ButtonState.SELECTED.equals(button.getTag())) {
                        button.setImageResource(R.drawable.radio_button_custom);
                    }
                }
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
                TextView time = findViewById(R.id.tiempo);
                time.setText(String.format("%02d:%02d", minuts, seconds));
                if((minuts*60+seconds)*1000<(tiempo*1000)/2){
                    time.setTextColor(getResources().getColor(R.color.naranjaPeligro));
                }
                if((minuts*60+seconds)*1000<(tiempo*1000)/3 ) {
                    time.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFinish() {
                handleTimeUp();
            }
        };
        timer.start();
    }

    private void handleTimeUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Senku.this);
        builder.setTitle("¡Se acabó el tiempo!")
                .setMessage("¿Quieres volver a jugar?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    this.finish();
                    this.startActivity(guardarDatosDeSession(this));
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                    this.startActivity(guardarDatosDeSession(new MainActivity()));
                })
                .setCancelable(false);

        AlertDialog gameOverDialog = builder.create();
        gameOverDialog.show();
    }

    private void handleGameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Senku.this);
        builder.setTitle("¡Has perdido!")
                .setMessage("¿Quieres volver a jugar?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    this.finish();
                    this.startActivity(guardarDatosDeSession(this));
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                    this.startActivity(guardarDatosDeSession(new MainActivity()));
                })
                .setCancelable(false);

        AlertDialog gameOverDialog = builder.create();
        gameOverDialog.show();
    }





    public void backToHubSenku(View view){
        startActivity(guardarDatosDeSession(new MainActivity()));
    }

    private Intent guardarDatosDeSession(AppCompatActivity activity){
        Intent inicio = new Intent(this, activity.getClass());
        inicio.putExtra("usuario",this.usuario);
        return inicio;

    }
}