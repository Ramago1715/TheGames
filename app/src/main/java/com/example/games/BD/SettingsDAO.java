package com.example.games.BD;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SettingsDAO {
    @Update
    void Update(Usuario usuario);

    @Query("SELECT fotoPerfil FROM usuarios WHERE email = :email")
    byte[] getFotoPerfilById(String email);
}
