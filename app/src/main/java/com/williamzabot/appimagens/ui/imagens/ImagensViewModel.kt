package com.williamzabot.appimagens.ui.imagens

import androidx.lifecycle.*
import com.williamzabot.appimagens.data.entity.Imagem
import com.williamzabot.appimagens.data.repository.imagem.ImagemRepository
import kotlinx.coroutines.launch

class ImagensViewModel(private val imagemRepository: ImagemRepository) : ViewModel() {

    private val _imagens = MutableLiveData<List<Imagem>>()
    val imagens: LiveData<List<Imagem>> get() = _imagens

    fun deletaImagem(id: Long) {
        viewModelScope.launch {
            imagemRepository.deletaImagem(id)
        }
    }

    fun getImagens() {
        viewModelScope.launch {
            _imagens.postValue(imagemRepository.getImagens())
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