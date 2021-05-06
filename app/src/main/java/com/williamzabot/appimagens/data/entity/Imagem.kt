package com.williamzabot.appimagens.data.entity

class Imagem(){
    var id: String = ""
    var imagemTitulo: String = ""
    var imagemURL: String = ""

    constructor(id: String, imagemTitulo: String, imagemURL: String) : this() {
        this.id = id
        this.imagemTitulo = imagemTitulo
        this.imagemURL = imagemURL
    }
}