package com.williamzabot.appimagens.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "imagens")
data class Imagem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val foto: ByteArray
)