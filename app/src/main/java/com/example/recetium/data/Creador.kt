package com.example.recetium.data

data class Creador(
    val idCreador: Int,
    val fechaCreacion: String,
    val contraseñaCreador: String,
    var nombre: String,
    var isBanned: Boolean
)