package com.williamzabot.appimagens.data.repository.imagem

import com.williamzabot.appimagens.data.dao.ImagemDAO
import com.williamzabot.appimagens.data.entity.Imagem

class ImagemRepositoryImpl(private val imagemDAO: ImagemDAO) : ImagemRepository {

    override suspend fun insereImagem(titulo: String, foto: ByteArray): Long {
        return imagemDAO.insert(
            Imagem(
                titulo = titulo,
                foto = foto
            )
        )
    }

    override suspend fun deletaImagem(id: Long) {
        imagemDAO.delete(id)
    }

    override suspend fun getImagens() = imagemDAO.getImagens()

}