package com.example.games.BD;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LeaderBoardDAO {
    @Query("SELECT bestScore FROM usuarios WHERE email = :email")
    int getUsuarioMayorPuntaje(String email);

    @Update
    void Update(Usuario usuario);
}
