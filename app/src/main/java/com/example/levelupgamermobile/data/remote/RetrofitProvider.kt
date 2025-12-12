package com.example.levelupgamermobile.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import android.os.Build

object RetrofitProvider {

    // Configuración para Emulador
    private const val EMULATOR_BASE_URL = "http://10.0.2.2:8080/"
    // Configuración para Dispositivo Físico (IP de tu PC)
    private const val DEVICE_BASE_URL = "http://192.168.100.11:8080/"

    // Detecta automáticamente si está corriendo en emulador o dispositivo real
    private val BASE_URL: String
        get() = if (isEmulator) EMULATOR_BASE_URL else DEVICE_BASE_URL

    private val isEmulator: Boolean
        get() = (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")

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
