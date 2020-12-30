package com.williamzabot.appimagens.ui.imagens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.williamzabot.appimagens.R
import com.williamzabot.appimagens.data.AppDatabase
import com.williamzabot.appimagens.data.repository.imagem.ImagemRepositoryImpl

class ImagensFragment : Fragment() {

    private lateinit var recyclerViewImagens: RecyclerView
    private lateinit var viewModel: ImagensViewModel
    private val imagensAdapter by lazy { ImagensAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("INICIO", "onCreate")
        viewModel = ViewModelProvider(
            viewModelStore, ImagensViewModel.ViewModelFactory(
                ImagemRepositoryImpl(
                    AppDatabase.getInstance(requireContext()).imagemDAO
                )
            )
        ).get(ImagensViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("INICIO", "onCreateView")
        return inflater.inflate(R.layout.fragment_imagens, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewImagens = view.findViewById(R.id.recyclerViewImagens)
        Log.i("INICIO", "onViewCreated")

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observaEventos()
        viewModel.getImagens()
    }


    private fun observaEventos() {
        viewModel.imagens.observe(viewLifecycleOwner){ imagens ->
            recyclerViewImagens.adapter = imagensAdapter
            imagensAdapter.imagens = imagens
        }
    }

}