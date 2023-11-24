package com.example.games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

public class game_2048 extends AppCompatActivity {
    private int buttonIdCounter = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048);

        GridLayout gridLayout = findViewById(R.id.GridLayout);

        int numColumns = 4;
        int numRows = 4;

        gridLayout.setColumnCount(numColumns);
        gridLayout.setRowCount(numRows);

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                Button button = new Button(this);
                button.setId(Integer.parseInt("Button" + buttonIdCounter++));
                button.setBackgroundTintList(getResources().getColorStateList(R.color.button_background_tint));
                button.setTextColor(getResources().getColor(R.color.button_text_color));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                params.height = 250;
                params.columnSpec = GridLayout.spec(col, 1f);
                params.rowSpec = GridLayout.spec(row, 1f);



                params.setGravity(Gravity.FILL);
                params.setGravity(Gravity.CENTER);
                button.setLayoutParams(params);
                gridLayout.addView(button);



            }

        }

    }
    public void backtohub2048(View view){
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);
    }
}