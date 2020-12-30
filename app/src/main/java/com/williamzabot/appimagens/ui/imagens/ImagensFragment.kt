package com.williamzabot.appimagens.ui.imagens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.williamzabot.appimagens.R
import com.williamzabot.appimagens.data.AppDatabase
import com.williamzabot.appimagens.data.entity.Imagem
import com.williamzabot.appimagens.data.repository.imagem.ImagemRepositoryImpl

class ImagensFragment : Fragment() {

    private lateinit var recyclerViewImagens: RecyclerView
    private lateinit var viewModel: ImagensViewModel
    private val imagensAdapter by lazy { ImagensAdapter() }
    private var listaImagens = listOf<Imagem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        return inflater.inflate(R.layout.fragment_imagens, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewImagens = view.findViewById(R.id.recyclerViewImagens)
        itemTouchHelper().attachToRecyclerView(recyclerViewImagens)
        observaEventos()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getImagens()
    }

    private fun observaEventos() {
        viewModel.imagens.observe(viewLifecycleOwner) { imagens ->
            listaImagens = imagens
            recyclerViewImagens.adapter = imagensAdapter
            imagensAdapter.imagens = imagens
        }

        viewModel.mensagem.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun itemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(
                    0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
                            or ItemTouchHelper.UP or ItemTouchHelper.DOWN
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val imagem = listaImagens[viewHolder.adapterPosition]
                viewModel.deletaImagem(imagem.id)
            }
        })
    }

}