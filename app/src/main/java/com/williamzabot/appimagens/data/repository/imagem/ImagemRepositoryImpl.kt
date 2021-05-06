package com.williamzabot.appimagens.data.repository.imagem

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import com.williamzabot.appimagens.data.singleton.References.storage

class ImagemRepositoryImpl : ImagemRepository {

    override fun insereImagem(uri : Uri, id : String): UploadTask {
       return storage.child(id).putFile(uri)
    }

    override fun deletaImagem(id: String): Task<Void> {
        return storage.child(id).delete()
    }

}