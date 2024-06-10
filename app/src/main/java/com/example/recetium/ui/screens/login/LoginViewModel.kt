package com.example.recetium.ui.screens.login


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetium.data.Consumidor
import com.example.recetium.data.Creador
import com.example.recetium.data.Receta
import com.example.recetium.data.RetrofitInstance
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginViewModel : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnabled = MutableLiveData<Boolean>()
    val loginEnabled: LiveData<Boolean> = _loginEnabled

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _isChef = MutableLiveData<Boolean>()
    val isChef: LiveData<Boolean> = _isChef

    private val _recipes = MutableLiveData<List<Receta>>()
    val recipes: LiveData<List<Receta>> = _recipes

    private val _creador = MutableLiveData<Creador?>()
    val creador: LiveData<Creador?> = _creador

    private val _consumidor = MutableLiveData<Consumidor?>()
    val consumidor: LiveData<Consumidor?> = _consumidor

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                val recetasResponse = RetrofitInstance.api.getRecetas()
                _recipes.postValue(recetasResponse)

                val creadoresResponse = RetrofitInstance.api.getCreadores()
                Log.d("LoginViewModel", "Creadores: $creadoresResponse")

                val consumidoresResponse = RetrofitInstance.api.getConsumidores()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onLoginChange(newUsername: String, password: String) {
        _username.value = newUsername
        _password.value = password
        _loginEnabled.value = isValidUsername(newUsername) && isValidPassword(password)
    }

    private fun isValidPassword(password: String): Boolean {
        return true
    }

    private fun isValidUsername(username: String): Boolean {
        val usernamePattern = Pattern.compile("^[a-zA-Z0-9_]{3,15}$")
        return usernamePattern.matcher(username).matches()
    }

    fun onLoginSelected() {
        viewModelScope.launch {
            val usernameValue = _username.value
            val passwordValue = _password.value
            if (usernameValue != null && passwordValue != null) {
                val creador = RetrofitInstance.api.getCreadores().find { it.nombre == usernameValue && it.contraseñaCreador == passwordValue }
                if (creador != null) {
                    Log.d("LoginViewModel", "Creador isBanned: ${creador.isBanned}")
                    _isChef.value = !creador.isBanned
                    _creador.value = creador
                    _consumidor.value = null
                    _loginSuccess.value = true
                    return@launch
                }

                val consumidor = RetrofitInstance.api.getConsumidores().find { it.nombre == usernameValue && it.contraseñaConsumidor == passwordValue }
                if (consumidor != null) {
                    _isChef.value = false
                    _creador.value = null
                    _consumidor.value = consumidor
                    _loginSuccess.value = true
                }
            }
        }
    }
}
