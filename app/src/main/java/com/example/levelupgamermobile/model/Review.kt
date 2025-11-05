package com.example.levelupgamermobile.model

/**
 * define la estructura de una sola resena de usuario.
 *
 * esta data class se guarda en la lista de [ProductRepository]
 * y se usa en [ProductDetailViewModel] y [MisOpinionesViewModel].
 */
data class Review(
    /**
     * un identificador unico (uuid) para esta resena.
     */
    val id: String,

    /**
     * el 'codigo' del [Producto] al que esta asociada esta resena.
     */
    val productId: String,

    /**
     * el email del usuario que escribio la resena.
     * actua como el identificador unico del usuario.
     */
    val userEmail: String,

    /**
     * el nombre de pila del usuario (ej. "bastian").
     * se guarda aqui para mostrarlo facilmente en la ui
     * sin necesidad de buscarlo.
     */
    val userName: String,

    /**
     * la calificacion en estrellas (de 1 a 5).
     */
    val calificacion: Int,

    /**
     * el comentario escrito por el usuario.
     */
    val comentario: String
)