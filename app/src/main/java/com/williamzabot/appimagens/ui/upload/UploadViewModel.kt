package com.williamzabot.appimagens.ui.upload

import androidx.lifecycle.*
import com.williamzabot.appimagens.data.repository.imagem.ImagemRepository
import kotlinx.coroutines.launch

class UploadViewModel(private val imagemRepository: ImagemRepository) : ViewModel() {

    private val _imagemSalva = MutableLiveData<Boolean>()
    val imagemSalva: LiveData<Boolean> get() = _imagemSalva


    fun addImagem(
        titulo: String,
        foto: ByteArray
    ) {
        viewModelScope.launch {
            imagemRepository.insereImagem(titulo, foto)
            _imagemSalva.postValue(true)
        }
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