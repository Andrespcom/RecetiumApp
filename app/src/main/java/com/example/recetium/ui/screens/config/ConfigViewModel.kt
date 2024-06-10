package com.example.recetium.ui.screens.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetium.data.Creador
import com.example.recetium.data.Consumidor
import com.example.recetium.data.RetrofitInstance
import com.example.recetium.ui.screens.home.HomeViewModel
import kotlinx.coroutines.launch

class ConfigViewModel(private val homeViewModel: HomeViewModel) : ViewModel() {
    private val _user = MutableLiveData<Any?>()
    val user: LiveData<Any?> = _user

    private val _creadores = MutableLiveData<List<Creador>>()
    val creadores: LiveData<List<Creador>> = _creadores

    private val _consumidores = MutableLiveData<List<Consumidor>>()
    val consumidores: LiveData<List<Consumidor>> = _consumidores

    init {
        loadProfiles()
    }

    private fun loadProfiles() {
        viewModelScope.launch {
            try {
                val creadoresResponse = RetrofitInstance.api.getCreadores()
                val consumidoresResponse = RetrofitInstance.api.getConsumidores()

                _creadores.postValue(creadoresResponse)
                _consumidores.postValue(consumidoresResponse)

                val currentUser = homeViewModel.creador.value ?: homeViewModel.consumidor.value
                _user.postValue(currentUser)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateCreadorNombre(nombre: String) {
        val creador = _user.value as? Creador ?: return
        creador.nombre = nombre
        _user.value = creador
    }

    fun updateConsumidorNombre(nombre: String) {
        val consumidor = _user.value as? Consumidor ?: return
        consumidor.nombre = nombre
        _user.value = consumidor
    }

    fun saveCreador() {
        val creador = _user.value as? Creador ?: return
        viewModelScope.launch {
            try {
                RetrofitInstance.api.updateCreador(creador.idCreador.toLong(), creador)
                loadProfiles()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveConsumidor() {
        val consumidor = _user.value as? Consumidor ?: return
        viewModelScope.launch {
            try {
                RetrofitInstance.api.updateConsumidor(consumidor.idConsumidor.toLong(), consumidor)
                loadProfiles()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}