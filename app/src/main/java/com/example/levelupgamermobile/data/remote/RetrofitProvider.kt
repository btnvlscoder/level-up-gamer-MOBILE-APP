package com.example.levelupgamermobile.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {

    // Cambia esto a tu IP local si usas emulador (10.0.2.2 es localhost de Android)
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Cliente HTTP con logging y timeouts
    private val client by lazy {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Loguea todo para debug
        }
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Instancia singleton de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // Conversor JSON <-> Objeto
            .build()
    }

    // Exposición del servicio API
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
