package com.example.recetium.ui.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetium.data.Consumidor
import com.example.recetium.data.Creador
import com.example.recetium.data.Receta
import com.example.recetium.data.RetrofitInstance
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _recipes = MutableLiveData<List<Receta>>()
    val recipes: LiveData<List<Receta>> = _recipes

    private val _creadores = MutableLiveData<List<Creador>>()
    val creadores: LiveData<List<Creador>> = _creadores

    private val _consumidores = MutableLiveData<List<Consumidor>>()
    val consumidores: LiveData<List<Consumidor>> = _consumidores

    private val _searchResults = MutableLiveData<List<String>>()
    val searchResults: LiveData<List<String>> = _searchResults

    init {
        loadAllData()
    }

    private fun loadAllData() {
        viewModelScope.launch {
            try {
                val recipesResponse = RetrofitInstance.api.getRecetas()
                _recipes.postValue(recipesResponse)

                val creadoresResponse = RetrofitInstance.api.getCreadores()
                _creadores.postValue(creadoresResponse)

                val consumidoresResponse = RetrofitInstance.api.getConsumidores()
                _consumidores.postValue(consumidoresResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun search(query: String) {
        val allResults = mutableListOf<String>()

        val filteredRecipes = _recipes.value?.filter { it.descripcion.contains(query, ignoreCase = true) }?.map { it.descripcion } ?: emptyList()
        allResults.addAll(filteredRecipes)

        val filteredCreadores = _creadores.value?.filter { it.nombre.contains(query, ignoreCase = true) }?.map { it.nombre } ?: emptyList()
        allResults.addAll(filteredCreadores)

        val filteredConsumidores = _consumidores.value?.filter { it.nombre.contains(query, ignoreCase = true) }?.map { it.nombre } ?: emptyList()
        allResults.addAll(filteredConsumidores)

        _searchResults.postValue(allResults)
    }
}