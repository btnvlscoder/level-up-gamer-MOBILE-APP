package com.example.levelupgamermobile.network

import com.example.levelupgamermobile.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Este objeto (Singleton) crea y gestiona nuestra instancia de Retrofit para toda la app.
 * Su configuración es genérica y sirve para todas las llamadas a la API,
 * incluyendo el login, obtener productos y, ahora, crear ventas.
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
    // IMPORTANTE: Tu Spring Boot debe estar corriendo y escuchando en el puerto 8080.
    // Esta URL base será el prefijo para todos los endpoints definidos en ApiService,
    // por ejemplo: "http://10.0.2.2:8080/" + "ventas/completa"

    /**
     * (2) El Cliente OkHttp:
     * Es el "transportista" que maneja las llamadas.
     * Le añadimos un 'logger' para poder ver las llamadas de red en el Logcat.
     * Esto es INVALUABLE para depurar: te mostrará el JSON exacto que se envía
     * y la respuesta que da el servidor (Ej: 201 Created, 404 Not Found, 500 Server Error).
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
     * Une la URL base (dónde está el servidor), el "transportista" (OkHttp, el cómo se envía) y
     * el "traductor" (Gson, qué formato se habla).
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            // (4) CONFIGURACIÓN CRÍTICA DEL TRADUCTOR (Gson):
            // GsonConverterFactory se encarga de convertir automáticamente:
            // - El objeto Kotlin (ej. CrearVentaRequest) a un String en formato JSON para enviar al backend.
            // - El String JSON de la respuesta del backend a un objeto Kotlin (si lo hubiera).
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * (5) El Servicio de API (El Contrato):
     * Finalmente, creamos una instancia de nuestra interfaz `ApiService`.
     * Esta es la instancia que usarán nuestros Repositorios para realizar las llamadas
     * de red (ej: `VentaRepository` llamará a `apiService.crearVentaCompleta(...)`).
     */
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
