package com.williamzabot.appimagens.ui.imagens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.williamzabot.appimagens.R
import com.williamzabot.appimagens.data.entity.Imagem
import java.io.ByteArrayInputStream

class ImagensAdapter : RecyclerView.Adapter<ImagensAdapter.ImagemViewHolder>() {

    var imagens = listOf<Imagem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagemViewHolder {
        return ImagemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_imagem, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImagemViewHolder, position: Int) =
        holder.bind(imagens[position])

    override fun getItemCount() = imagens.size

    inner class ImagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView = itemView.findViewById<ImageView>(R.id.imagem)
        private val tituloImagem = itemView.findViewById<TextView>(R.id.titulo_imagem)

        fun bind(imagem: Imagem) {
            tituloImagem.text = imagem.titulo
            val foto = converteByteArrayParaBitmap(imagem.foto)
            imageView.setImageBitmap(foto)
        }

        private fun converteByteArrayParaBitmap(imgByteArray: ByteArray): Bitmap {
            val stream = ByteArrayInputStream(imgByteArray)
            return BitmapFactory.decodeStream(stream)
        }
    }
}