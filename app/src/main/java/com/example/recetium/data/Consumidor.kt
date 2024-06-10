package com.example.recetium.data

data class Consumidor(
    val idConsumidor: Int,
    val fechaCreacion: String,
    val contraseñaConsumidor: String,   //hay que ponerle lo de @json name = tal
    var nombre: String
)