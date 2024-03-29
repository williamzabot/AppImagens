package com.williamzabot.appimagens.ui.upload

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.williamzabot.appimagens.R
import com.williamzabot.appimagens.data.AppDatabase
import com.williamzabot.appimagens.data.repository.imagem.ImagemRepositoryImpl
import com.williamzabot.appimagens.extensions.hideKeyboard
import java.io.ByteArrayOutputStream

const val ESCOLHER_IMAGEM_REQUISICAO = 1
const val TIRAR_FOTO_REQUISICAO = 2

class UploadFragment : Fragment() {

    private lateinit var editTextTituloImg: TextInputEditText
    private lateinit var imageView: AppCompatImageView
    private lateinit var botaoSalvar: MaterialButton
    private lateinit var botaoAbreGaleria: MaterialButton
    private lateinit var botaoAbreCamera: MaterialButton
    private var imageBitmap: Bitmap? = null
    private lateinit var viewModel: UploadViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        observaEventos()
        eventoCliques()
    }

    private fun initView(view: View) {
        viewModel = ViewModelProvider(
            viewModelStore, UploadViewModel.ViewModelFactory(
                ImagemRepositoryImpl(
                    AppDatabase.getInstance(requireContext()).imagemDAO
                )
            )
        ).get(UploadViewModel::class.java)

        editTextTituloImg = view.findViewById(R.id.edittext_titulo_imagem)
        imageView = view.findViewById(R.id.imagem_upload)
        botaoSalvar = view.findViewById(R.id.botao_salvar_imagem)
        botaoAbreGaleria = view.findViewById(R.id.botao_abre_galeria)
        botaoAbreCamera = view.findViewById(R.id.botao_abre_camera)
    }

    private fun observaEventos() {
        viewModel.imagemSalva.observe(viewLifecycleOwner,  Observer {
            Toast.makeText(context, "Imagem salva com sucesso", Toast.LENGTH_SHORT).show()
            clearUI()
        })

    }

    private fun clearUI() {
        (requireActivity() as AppCompatActivity).hideKeyboard()
        imageView.setImageDrawable(getDrawable(requireContext(), android.R.drawable.ic_menu_camera))
        editTextTituloImg.text = null
        imageBitmap = null
    }

    private fun eventoCliques() {
        cliqueBotaoGaleria()
        cliqueBotaoCamera()
        cliqueBotaoSalvar()
    }

    private fun cliqueBotaoCamera() {
        botaoAbreCamera.setOnClickListener {
            val cam = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cam.resolveActivity(requireContext().packageManager) != null) {
                startActivityForResult(cam, TIRAR_FOTO_REQUISICAO)
            }
        }
    }

    private fun cliqueBotaoGaleria() {
        botaoAbreGaleria.setOnClickListener {
            val gal = Intent()
            gal.type = "image/*"
            gal.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(gal, ESCOLHER_IMAGEM_REQUISICAO)
        }
    }

    private fun cliqueBotaoSalvar() {
        botaoSalvar.setOnClickListener {
            if (imageBitmap != null) {
                val byteArray = converteBitmapParaByteArray(imageBitmap!!)
                var titulo: String? = null
                editTextTituloImg.text.toString().run {
                    if (this.isNotEmpty()) {
                        titulo = this
                    }
                }
                viewModel.addImagem(titulo ?: "Sem título", byteArray)
            } else {
                Toast.makeText(context, "Imagem não encontrada!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun converteBitmapParaByteArray(imgBitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ESCOLHER_IMAGEM_REQUISICAO && resultCode == Activity.RESULT_OK && data != null
            && data.data != null
        ) {
            val imagemUri = data.data
            imageView.setImageURI(imagemUri)
            imageBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imagemUri)
        } else if (requestCode == TIRAR_FOTO_REQUISICAO && resultCode == Activity.RESULT_OK && data != null) {
            imageBitmap = data.extras?.get("data") as? Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

}