package com.williamzabot.appimagens.ui.upload

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.williamzabot.appimagens.data.entity.Imagem
import com.williamzabot.appimagens.data.repository.imagem.ImagemRepository
import com.williamzabot.appimagens.data.singleton.References.realtime
import com.williamzabot.appimagens.data.singleton.References.storage

class UploadViewModel(private val imagemRepository: ImagemRepository) : ViewModel() {

    private val _imagemSalva = MutableLiveData<Boolean>()
    val imagemSalva: LiveData<Boolean> get() = _imagemSalva

    private val _erroInserir = MutableLiveData<Boolean>()
    val erroInserir: LiveData<Boolean> get() = _erroInserir

    private val _erroDownload = MutableLiveData<Boolean>()
    val erroDownload: LiveData<Boolean> get() = _erroDownload

    private val _erroRealtime = MutableLiveData<Boolean>()
    val erroRealtime: LiveData<Boolean> get() = _erroRealtime


    fun addImagem(
        titulo: String,
        foto: Uri
    ) {
        val id = System.currentTimeMillis().toString()
        imagemRepository.insereImagem(foto, id).addOnSuccessListener {
            storage.child("images/$id").downloadUrl.addOnSuccessListener { downloadUrl ->
                val imagem = Imagem(id, titulo, downloadUrl.toString())
                realtime.child(imagem.id).setValue(imagem)
                    .addOnSuccessListener {
                        _imagemSalva.postValue(true)
                    }.addOnFailureListener {
                        _erroRealtime.postValue(true)
                    }
            }.addOnFailureListener { _erroDownload.postValue(true) }
        }.addOnFailureListener { _erroInserir.postValue(true) }
    }

    class ViewModelFactory(private val dataSource: ImagemRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
                return modelClass.getConstructor(ImagemRepository::class.java)
                    .newInstance(dataSource)
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}