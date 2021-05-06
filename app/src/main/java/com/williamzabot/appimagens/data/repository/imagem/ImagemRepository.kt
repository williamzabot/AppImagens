package com.williamzabot.appimagens.data.repository.imagem

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import com.williamzabot.appimagens.data.entity.Imagem

interface ImagemRepository {

    fun insereImagem(uri: Uri, id : String): UploadTask

    fun deletaImagem(id: String): Task<Void>
}