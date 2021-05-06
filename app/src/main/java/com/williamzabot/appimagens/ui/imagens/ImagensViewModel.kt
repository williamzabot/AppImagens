package com.williamzabot.appimagens.ui.imagens

import androidx.lifecycle.*
import com.williamzabot.appimagens.data.entity.Imagem
import com.williamzabot.appimagens.data.repository.imagem.ImagemRepository
import com.williamzabot.appimagens.data.singleton.References.realtime
import kotlinx.coroutines.launch

class ImagensViewModel(private val imagemRepository: ImagemRepository) : ViewModel() {

    private val _imagemDeletada = MutableLiveData<String>()
    val imagemDeletada: LiveData<String> get() = _imagemDeletada

    private val _falhaAoDeletar = MutableLiveData<Boolean>()
    val falhaAoDeletar: LiveData<Boolean> get() = _falhaAoDeletar

    fun deletaImagem(id: String) {
        imagemRepository.deletaImagem(id).addOnSuccessListener {
            realtime.child(id).removeValue().addOnSuccessListener {
                _imagemDeletada.postValue("Imagem deletada com sucesso!")
            }.addOnFailureListener {
                _falhaAoDeletar.postValue(true)
            }
        }.addOnFailureListener {
            _falhaAoDeletar.postValue(true)
        }
    }

    class ViewModelFactory(private val dataSource: ImagemRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ImagensViewModel::class.java)) {
                return modelClass.getConstructor(ImagemRepository::class.java)
                    .newInstance(dataSource)
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}