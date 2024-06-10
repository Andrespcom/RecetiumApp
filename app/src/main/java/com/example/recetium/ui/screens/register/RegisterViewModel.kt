package com.example.recetium.ui.screens.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetium.data.Consumidor
import com.example.recetium.data.RetrofitInstance
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _registerEnabled = MutableLiveData<Boolean>()
    val registerEnabled: LiveData<Boolean> = _registerEnabled

    private val _consumidores = MutableLiveData<List<Consumidor>>()
    val consumidores: LiveData<List<Consumidor>> = _consumidores

    init {
        loadProfiles()
    }

    private fun loadProfiles() {
        viewModelScope.launch {
            try {
                val consumidoresList = RetrofitInstance.api.getConsumidores()
                _consumidores.postValue(consumidoresList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
        validateForm()
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
        validateForm()
    }

    private fun validateForm() {
        val usernameValid = !_username.value.isNullOrBlank()
        val passwordValid = !_password.value.isNullOrBlank()
        _registerEnabled.value = usernameValid && passwordValid
    }

    fun onRegisterSelected(onSuccess: () -> Unit) {
        val newUsername = _username.value ?: return
        val newPassword = "${_password.value}" ?: return

        val consumidorExists = _consumidores.value?.any { it.nombre == newUsername } == true

        if (consumidorExists) {
            // Handle user already exists
            return
        }

        viewModelScope.launch {
            try {
                val newConsumidor = Consumidor(
                    idConsumidor = 0,
                    nombre = newUsername,
                    contraseñaConsumidor = newPassword,
                    fechaCreacion = "2024-06-02"
                )
                RetrofitInstance.api.postConsumidor(newConsumidor)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}