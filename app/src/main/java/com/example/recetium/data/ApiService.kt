package com.example.recetium.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Retrofit
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/recetas")
    suspend fun getRecetas(): List<Receta>

    @GET("/creadores")
    suspend fun getCreadores(): List<Creador>

    @GET("/consumidores")
    suspend fun getConsumidores(): List<Consumidor>

    @POST("/recetas")
    suspend fun postReceta(@Body receta: Receta): Receta
    @POST("/consumidores")
    suspend fun postConsumidor(@Body consumidor: Consumidor) :Consumidor

    @PUT("/creadores/{id}")
    suspend fun updateCreador(@Path("id") id: Long, @Body creador: Creador): Creador

    @PUT("/consumidores/{id}")
    suspend fun updateConsumidor(@Path("id") id: Long, @Body consumidor: Consumidor): Consumidor

    @Multipart
    @POST("/v1_1/recetium/image/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: RequestBody
    ): CloudinaryResponse
}

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    private const val CLOUDINARY_URL = "https://api.cloudinary.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    val cloudinaryApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(CLOUDINARY_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}