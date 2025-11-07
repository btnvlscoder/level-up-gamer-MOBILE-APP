package com.example.levelupgamermobile.network

import com.example.levelupgamermobile.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * gestiona la configuracion y creacion de la instancia de retrofit.
 *
 * se implementa como un 'object' (singleton) para asegurar que
 * toda la aplicacion use una unica instancia de [OkHttpClient]
 * y [Retrofit].
 *
 * expone la instancia de [ApiService] ya configurada
 * para que los repositorios (como [AuthRepository]) la consuman.
 */
object RetrofitProvider {

    // la ip '10.0.2.2' es una direccion especial que usa el
    // emulador de android para conectarse al 'localhost' de la
    // maquina (pc) que lo esta ejecutando.
    //
    // importante: el backend spring boot debe estar corriendo
    // en el puerto 8080 para que esto funcione.
    private const val BASE_URL = "http://10.0.2.2:8080/"

    /**
     * crea el cliente [OkHttpClient] usando 'by lazy' para
     * que se instancie una sola vez cuando se necesite.
     *
     * incluye un interceptor de logging que imprime el 'body'
     * de todas las peticiones y respuestas en el logcat.
     * esto es fundamental para la depuracion de la red.
     *
     * tambien define tiempos de espera (timeouts) para
     * evitar que la app se congele si el servidor no responde.
     */
    private val client by lazy {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(30, TimeUnit.SECONDS) // tiempo de espera para conectar
            .readTimeout(30, TimeUnit.SECONDS)    // tiempo de espera para leer respuesta
            .writeTimeout(30, TimeUnit.SECONDS)   // tiempo de espera para escribir peticion
            .build()
    }

    /**
     * crea la instancia principal de [Retrofit] usando 'by lazy'.
     *
     * configura la url base, el cliente [OkHttpClient]
     * y el convertidor de json.
     *
     * usamos [GsonConverterFactory] porque los dtos del backend
     * (ej. [UsuarioDTO]) usan la serializacion estandar de java/gson,
     * no la de kotlinx-serialization.
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * expone publicamente la implementacion de nuestra interfaz [ApiService].
     *
     * los repositorios (ej. [AuthRepository]) llamaran a esta
     * variable para realizar las peticiones de red.
     */
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}