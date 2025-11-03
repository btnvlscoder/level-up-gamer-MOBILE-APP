package com.example.levelupgamermobile.network

import com.example.levelupgamermobile.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Este objeto (Singleton) crea y gestiona nuestra
 * instancia de Retrofit para toda la app.
 */
object RetrofitProvider {

    // (1) CONFIGURACIÓN CRÍTICA DE IP:
    // El emulador de Android corre en su propia "máquina virtual".
    // "localhost" o "127.0.0.1" apuntarían AL EMULADOR, no a tu PC.
    //
    // Android nos da una IP especial para hablar con el "localhost" de la
    // computadora que lo está corriendo (tu PC):
    private const val BASE_URL = "http://10.0.2.2:8080/"
    //
    // IMPORTANTE: Asegúrate de que tu Spring Boot esté corriendo en el puerto 8080.

    /**
     * (2) El Cliente OkHttp:
     * Es el "transportista" que maneja las llamadas.
     * Le añadimos un 'logger' para poder ver las llamadas de red
     * en el Logcat (¡súper útil para depurar!).
     */
    private val client by lazy {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Muestra todo: headers, body, etc.
        }
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(30, TimeUnit.SECONDS) // Tiempo de espera para conectar
            .readTimeout(30, TimeUnit.SECONDS)    // Tiempo de espera para leer datos
            .build()
    }

    /**
     * (3) El Constructor de Retrofit:
     * Une la URL base, el "transportista" (client) y
     * el "traductor" (Gson).
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            // (4) CONFIGURACIÓN CRÍTICA DE TRADUCTOR:
            // Usamos GsonConverterFactory porque tus DTOs
            // son de Java/Lombok y Retrofit los traduce
            // usando la librería Gson.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * (5) El Servicio de API:
     * Finalmente, creamos una instancia de nuestro "contrato" (ApiService)
     * que la app pueda usar.
     */
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}