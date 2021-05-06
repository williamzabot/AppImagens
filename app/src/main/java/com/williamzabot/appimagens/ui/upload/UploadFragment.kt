package com.williamzabot.appimagens.ui.upload

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import com.williamzabot.appimagens.R
import com.williamzabot.appimagens.data.repository.imagem.ImagemRepositoryImpl

import com.williamzabot.appimagens.extensions.hideKeyboard
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

const val ESCOLHER_IMAGEM_REQUISICAO = 1
const val TIRAR_FOTO_REQUISICAO = 2

class UploadFragment : Fragment() {

    private var imagemUri: Uri? = null
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
            viewModelStore, UploadViewModel.ViewModelFactory(ImagemRepositoryImpl())
        ).get(UploadViewModel::class.java)
        editTextTituloImg = view.findViewById(R.id.edittext_titulo_imagem)
        imageView = view.findViewById(R.id.imagem_upload)
        botaoSalvar = view.findViewById(R.id.botao_salvar_imagem)
        botaoAbreGaleria = view.findViewById(R.id.botao_abre_galeria)
        botaoAbreCamera = view.findViewById(R.id.botao_abre_camera)
    }

    private fun observaEventos() {
        viewModel.imagemSalva.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "Imagem salva com sucesso", Toast.LENGTH_SHORT).show()
            clearUI()
        })

        viewModel.erroInserir.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "Erro ao inserir imagem no Cloud Storage", Toast.LENGTH_SHORT).show()
        })

        viewModel.erroDownload.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "Erro ao fazer download da URL", Toast.LENGTH_SHORT).show()
        })

        viewModel.erroRealtime.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "Erro ao salvar dados no banco", Toast.LENGTH_SHORT).show()
        })

    }

    private fun clearUI() {
        (requireActivity() as AppCompatActivity).hideKeyboard()
        imageView.setImageDrawable(getDrawable(requireContext(), android.R.drawable.ic_menu_camera))
        editTextTituloImg.text = null
        imagemUri = null
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
            if (imagemUri != null) {
                var titulo: String? = null
                editTextTituloImg.text.toString().run {
                    if (this.isNotEmpty()) {
                        titulo = this
                    }
                }
                viewModel.addImagem(titulo ?: "Sem título", imagemUri!!)
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

    private fun retornaUri(imageBitmap: Bitmap): Uri {
        val imagemBytes = converteBitmapParaByteArray(imageBitmap)
        val fileRetornado = converteParaFileDentroDoAndroid(imagemBytes)
        return Uri.parse(fileRetornado.toURI().toString())
    }

    private fun converteParaFileDentroDoAndroid(imagemBytes: ByteArray): File {
        val file = File(requireContext().filesDir, "${System.currentTimeMillis()}.jpg")
        val os = FileOutputStream(file)
        os.write(imagemBytes)
        os.close()
        return file
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ESCOLHER_IMAGEM_REQUISICAO && resultCode == Activity.RESULT_OK && data != null
            && data.data != null
        ) {
            data.data?.let {
                imagemUri = it
                imageView.setImageURI(it)
            }
        } else if (requestCode == TIRAR_FOTO_REQUISICAO && resultCode == Activity.RESULT_OK && data != null) {
            imageBitmap = data.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                imageView.setImageBitmap(it)
                imagemUri = retornaUri(it)
            }
        }
    }

}