package com.example.recetium.ui.screens.config

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetium.data.Creador
import com.example.recetium.data.Consumidor
import com.example.recetium.data.Receta
import com.example.recetium.data.RetrofitInstance
import com.example.recetium.ui.screens.home.HomeViewModel
import kotlinx.coroutines.launch

class ConfigViewModel(private val homeViewModel: HomeViewModel) : ViewModel() {

    private val _user = MutableLiveData<Any?>()
    val user: LiveData<Any?> = _user

    private val _recetas = MutableLiveData<List<Receta>>()
    val recetas: LiveData<List<Receta>> = _recetas

    // Estado para el nombre y contraseña
    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private val _contrasena = MutableLiveData<String>()
    val contrasena: LiveData<String> = _contrasena

    // Estado para verificar si el usuario está baneado
    private val _isBanned = MutableLiveData<Boolean>()
    val isBanned: LiveData<Boolean> = _isBanned

    init {
        Log.d("ConfigViewModel", "Initializing ConfigViewModel")
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val currentUser = homeViewModel.creador.value ?: homeViewModel.consumidor.value
        Log.d("ConfigViewModel", "Current user: $currentUser")
        _user.value = currentUser
        currentUser?.let {
            _nombre.value = if (it is Creador) it.nombre else (it as Consumidor).nombre
            _contrasena.value = if (it is Creador) it.contraseñaCreador else (it as Consumidor).contraseñaConsumidor
            if (it is Creador) {
                _isBanned.value = it.isBanned
                Log.d("ConfigViewModel", "Creador is banned: ${it.isBanned}")
                if (!it.isBanned) {
                    loadRecetasByCreador(it.idCreador.toLong())
                }
            } else if (it is Consumidor) {
                _isBanned.value = false
                loadRepostsByConsumidor(it.idConsumidor.toLong())
            }
        } ?: run {
            // If currentUser is null, set isBanned to true to handle banned or invalid user state
            _isBanned.value = true
        }
    }

    private fun loadRecetasByCreador(creadorId: Long) {
        viewModelScope.launch {
            try {
                Log.d("ConfigViewModel", "Loading recipes for creator with ID: $creadorId")
                val response = RetrofitInstance.api.getRecetasByCreador(creadorId)
                _recetas.postValue(response)
                Log.d("ConfigViewModel", "Loaded recipes: $response")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadRepostsByConsumidor(consumidorId: Long) {
        viewModelScope.launch {
            try {
                Log.d("ConfigViewModel", "Loading reposts for consumer with ID: $consumidorId")
                val response = RetrofitInstance.api.getRepostsByConsumidor(consumidorId)
                _recetas.postValue(response)
                Log.d("ConfigViewModel", "Loaded reposts: $response")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateNombre(nombre: String) {
        Log.d("ConfigViewModel", "Updating name to: $nombre")
        _nombre.value = nombre
        _user.value?.let {
            if (it is Creador) {
                it.nombre = nombre
                _user.value = it
            } else if (it is Consumidor) {
                it.nombre = nombre
                _user.value = it
            }
        }
    }

    fun saveUser() {
        _user.value?.let {
            viewModelScope.launch {
                try {
                    when (it) {
                        is Creador -> {
                            Log.d("ConfigViewModel", "Saving creator: $it")
                            RetrofitInstance.api.updateCreador(it.idCreador.toLong(), it)
                        }
                        is Consumidor -> {
                            Log.d("ConfigViewModel", "Saving consumer: $it")
                            RetrofitInstance.api.updateConsumidor(it.idConsumidor.toLong(), it)
                        }
                    }
                    loadUserProfile()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun updateContrasena(contrasena: String) {
        Log.d("ConfigViewModel", "Updating password to: $contrasena")
        _contrasena.value = contrasena
        _user.value?.let {
            if (it is Creador) {
                it.contraseñaCreador = contrasena
                _user.value = it
            } else if (it is Consumidor) {
                it.contraseñaConsumidor = contrasena
                _user.value = it
            }
        }
    }

    fun deleteReceta(recetaId: Long) {
        viewModelScope.launch {
            try {
                Log.d("ConfigViewModel", "Deleting recipe with ID: $recetaId")
                val response = RetrofitInstance.api.deleteReceta(recetaId)
                if (response.isSuccessful) {
                    loadUserProfile()  // Recargar el perfil después de eliminar
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteRepost(repostId: Long) {
        viewModelScope.launch {
            try {
                Log.d("ConfigViewModel", "Deleting repost with ID: $repostId")
                val response = RetrofitInstance.api.deleteRepost(repostId)
                if (response.isSuccessful) {
                    loadUserProfile()  // Recargar el perfil después de eliminar
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}