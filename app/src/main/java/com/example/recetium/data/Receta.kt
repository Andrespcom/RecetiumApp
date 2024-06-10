package com.example.recetium.data

data class Receta(
    val idReceta: Int,
    val creador: Creador?,
    val fechaPublicacionReceta: String,
    val strImage: String,
    val ingredientes: String,
    val descripcion: String
) {
    constructor(
        idReceta: Int,
        creadorId: Int,
        fechaPublicacionReceta: String,
        strImage: String,
        ingredientes: String,
        descripcion: String
    ) : this(
        idReceta,
        Creador(idCreador = creadorId, fechaCreacion = "", contraseñaCreador = "", nombre = "", isBanned = false),
        fechaPublicacionReceta,
        strImage,
        ingredientes,
        descripcion
    )
}