package com.example.recetium.ui.screens.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recetium.data.Receta

class CardViewModel : ViewModel() {

    private val _selectedReceta = MutableLiveData<Receta>()
    val selectedReceta: LiveData<Receta> get() = _selectedReceta

    fun selectReceta(receta: Receta) {
        _selectedReceta.value = receta
    }
}