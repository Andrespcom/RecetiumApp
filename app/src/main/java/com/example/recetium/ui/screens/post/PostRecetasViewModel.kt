package com.example.recetium.ui.screens.post

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.recetium.data.Creador
import com.example.recetium.data.Receta
import com.example.recetium.data.RetrofitInstance
import com.example.recetium.ui.screens.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class PostRecetasViewModel : ViewModel() {

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _ingredients = MutableLiveData<String>()
    val ingredients: LiveData<String> = _ingredients

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri

    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean> = _uploadStatus

    private val _creadores = MutableLiveData<List<Creador>>()
    val creadores: LiveData<List<Creador>> = _creadores

    private var creadorTemp: Creador? = null

    fun setDescription(desc: String) {
        _description.value = desc
    }

    fun setIngredients(ing: String) {
        _ingredients.value = ing
    }

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun setCreadorTemp(creador: Creador) {
        this.creadorTemp = creador
    }

    fun initialize(homeViewModel: HomeViewModel) {
        viewModelScope.launch {
            try {
                val creadoresList = RetrofitInstance.api.getCreadores()
                _creadores.postValue(creadoresList)

                val currentUser = homeViewModel.creador.value
                val currentCreador = creadoresList.find { it.nombre == currentUser?.nombre }
                currentCreador?.let { setCreadorTemp(it) }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun postReceta(onSuccess: () -> Unit) {
        val desc = _description.value ?: return
        val ing = _ingredients.value ?: return
        val uri = _imageUri.value
        val creador = creadorTemp ?: return

        viewModelScope.launch {
            try {
                val imageUrl = uri?.let {
                    // Convert Uri to File
                    val imageFile = File(it.path ?: "")
                    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                    val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
                    val uploadPreset = RequestBody.create("text/plain".toMediaTypeOrNull(), "your_upload_preset")

                    // Upload the image to Cloudinary or your backend
                    RetrofitInstance.api.uploadImage(body, uploadPreset).url
                } ?: ""

                // Create the new recipe
                val newReceta = Receta(
                    idReceta = 0, // This will be generated by the backend
                    creador = creador, // Use the creador from HomeViewModel
                    fechaPublicacionReceta = "2024-06-02", // Replace with the current date
                    strImage = imageUrl,
                    ingredientes = ing,
                    descripcion = desc
                )

                // Post the new recipe
                RetrofitInstance.api.postReceta(newReceta)
                _uploadStatus.postValue(true)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                _uploadStatus.postValue(false)
            }
        }
    }
}