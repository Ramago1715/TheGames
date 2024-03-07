package com.example.games.BD;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Usuario.class},version = 1)
public abstract class GameDB extends RoomDatabase{
    private static GameDB INSTANCE;

    public static synchronized GameDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    GameDB.class, "BD").build();
        }
        return INSTANCE;
    }
    public abstract LogInDAO LogInDAO();
    public abstract  RegisterDAO RegisterDAO();
    public abstract LeaderBoardDAO LeaderBoardDAO();
    public abstract SettingsDAO SettingsDAO();
}