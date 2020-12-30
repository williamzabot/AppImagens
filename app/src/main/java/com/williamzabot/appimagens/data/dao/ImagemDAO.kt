package com.williamzabot.appimagens.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.williamzabot.appimagens.data.entity.Imagem

@Dao
interface ImagemDAO {

    @Insert
    suspend fun insert(imagem: Imagem): Long

    @Query("DELETE FROM imagens WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM imagens")
    suspend fun getImagens(): List<Imagem>
}