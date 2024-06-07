package com.example.recetium.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetium.data.Creador
import com.example.recetium.data.Receta
import com.example.recetium.data.RetrofitInstance
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Receta>>()
    val recipes: LiveData<List<Receta>> = _recipes

    private val _isChef = MutableLiveData<Boolean>()
    val isChef: LiveData<Boolean> = _isChef

    private val _creador = MutableLiveData<Creador?>()
    val creador: LiveData<Creador?> = _creador

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getRecetas()
                _recipes.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setUserType(isChef: Boolean, creador: Creador?) {
        _isChef.value = isChef
        _creador.value = creador
    }
}