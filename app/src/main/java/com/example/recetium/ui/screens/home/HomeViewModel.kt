package com.example.recetium.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetium.data.Consumidor
import com.example.recetium.data.Creador
import com.example.recetium.data.Receta
import com.example.recetium.data.Repost
import com.example.recetium.data.RetrofitInstance
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Receta>>()
    val recipes: LiveData<List<Receta>> = _recipes

    private val _isChef = MutableLiveData<Boolean>()
    val isChef: LiveData<Boolean> = _isChef

    private val _creador = MutableLiveData<Creador?>()
    val creador: LiveData<Creador?> = _creador

    private val _consumidor = MutableLiveData<Consumidor?>()
    val consumidor: LiveData<Consumidor?> = _consumidor

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

    fun setUserType(isChef: Boolean, user: Any?) {
        if (isChef && user is Creador) {
            _isChef.value = !user.isBanned
            _creador.value = user
        } else {
            _isChef.value = false
            _creador.value = null
            _consumidor.value = user as Consumidor?
        }
    }

    fun clearUser() {
        _isChef.value = null
        _creador.value = null
        _consumidor.value = null
    }

    fun createRepost(receta: Receta, consumidor: Consumidor, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                val currentDate = Date()
                val formattedDate = sdf.format(currentDate)

                val repost = Repost(
                    idRepost = 0,
                    idReceta = receta.idReceta.toLong(),
                    idCreador = receta.creador?.idCreador?.toLong() ?: 1,
                    idConsumidor = consumidor.idConsumidor.toLong(),
                    fechaRepost = formattedDate
                )
                val response = RetrofitInstance.api.createRepost(repost)
                onResult(response.isSuccessful)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }
}