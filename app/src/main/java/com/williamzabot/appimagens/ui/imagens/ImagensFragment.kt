package com.williamzabot.appimagens.ui.imagens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.williamzabot.appimagens.R
import com.williamzabot.appimagens.data.entity.Imagem
import com.williamzabot.appimagens.data.repository.imagem.ImagemRepositoryImpl
import com.williamzabot.appimagens.data.singleton.References.realtime

class ImagensFragment : Fragment() {

    private lateinit var recyclerViewImagens: RecyclerView
    private lateinit var viewModel: ImagensViewModel
    private val imagensAdapter by lazy { ImagensAdapter() }
    private var listaImagens = mutableListOf<Imagem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            viewModelStore, ImagensViewModel.ViewModelFactory(
                ImagemRepositoryImpl()
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
        recyclerViewImagens.adapter = imagensAdapter
        itemTouchHelper().attachToRecyclerView(recyclerViewImagens)
        observaEventos()
    }

    override fun onResume() {
        super.onResume()
        getImagens()
    }

    fun getImagens() {
        listaImagens.clear()
        realtime.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val img = postSnapshot.getValue(Imagem::class.java)
                    img?.let {
                        listaImagens.add(img)
                    }
                }
                imagensAdapter.imagens = listaImagens
            }
        })
    }

    private fun observaEventos() {
        viewModel.imagemDeletada.observe(viewLifecycleOwner, Observer {msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            getImagens()
        })

        viewModel.falhaAoDeletar.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "Falha ao deletar", Toast.LENGTH_SHORT).show()
            getImagens()
        })
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