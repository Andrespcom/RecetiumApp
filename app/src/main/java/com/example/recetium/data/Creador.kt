package com.example.recetium.data

import com.google.gson.annotations.SerializedName

data class Creador(
    val idCreador: Int,
    val fechaCreacion: String,
    var contraseñaCreador: String,
    var nombre: String,
    @SerializedName("banned")
    var isBanned: Boolean
)