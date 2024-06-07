package com.example.recetium.ui.screens.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetium.data.Creador
import com.example.recetium.data.Consumidor
import com.example.recetium.data.RetrofitInstance
import kotlinx.coroutines.launch

class ConfigViewModel : ViewModel() {
    private val _creadores = MutableLiveData<List<Creador>>()
    val creadores: LiveData<List<Creador>> = _creadores

    private val _consumidores = MutableLiveData<List<Consumidor>>()
    val consumidores: LiveData<List<Consumidor>> = _consumidores

    init {
        loadProfiles()
    }

    fun loadProfiles() {
        viewModelScope.launch {
            try {
                val creadoresResponse = RetrofitInstance.api.getCreadores()
                val consumidoresResponse = RetrofitInstance.api.getConsumidores()

                // Assuming RetrofitInstance.api.getCreadores() returns a list of Creador with their associated recetas
                _creadores.postValue(creadoresResponse)
                _consumidores.postValue(consumidoresResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}