package com.example.games.BD;

import androidx.room.Dao;
import androidx.room.Query;
@Dao
public interface LogInDAO {
    @Query("Select * From usuarios WHERE email = :gmail AND password = :password")
    Usuario login(String gmail,String password);

    @Query("SELECT EXISTS(SELECT 1 FROM usuarios WHERE email = :email AND password = :password)")
    boolean existUser(String email,String password);
}
