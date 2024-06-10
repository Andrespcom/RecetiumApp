package com.example.recetium.data

import java.util.Date

data class Repost(
    val idRepost: Int,
    val idReceta: Long,
    val idCreador: Long,
    val idConsumidor: Long,
    val fechaRepost: String
)