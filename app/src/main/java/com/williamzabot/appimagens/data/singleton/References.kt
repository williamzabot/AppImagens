package com.williamzabot.appimagens.data.singleton

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

object References {

    val storage = FirebaseStorage.getInstance().reference
    val realtime = FirebaseDatabase.getInstance().getReference("imagens/")

}