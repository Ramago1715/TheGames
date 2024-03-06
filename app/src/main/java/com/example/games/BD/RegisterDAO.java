package com.example.games.BD;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
@Dao
public interface RegisterDAO {

    @Query("SELECT EXISTS(SELECT 1 FROM usuarios WHERE email = :email)")
    boolean alreadyExist(String email);

    @Insert
    void insert(Usuario usuario);

}
