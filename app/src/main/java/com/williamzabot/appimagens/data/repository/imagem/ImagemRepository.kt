package com.williamzabot.appimagens.data.repository.imagem

import com.williamzabot.appimagens.data.entity.Imagem

interface ImagemRepository {

    suspend fun insereImagem(titulo : String, foto : ByteArray): Long

    suspend fun deletaImagem(id: Long)

    suspend fun getImagens(): List<Imagem>
}