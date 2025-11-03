package com.example.levelupgamermobile

import android.app.Application
import com.example.levelupgamermobile.data.SessionManager

/**
 * Clase de Aplicación personalizada.
 * Se inicia antes que cualquier Activity.
 */
class LevelUpGamerApp : Application() {

    // (1) Creamos una "propiedad estática" (companion object)
    // para nuestro SessionManager.
    companion object {
        lateinit var sessionManager: SessionManager
            private set // "private set" = solo esta clase puede asignarle un valor
    }

    /**
     * (2) "onCreate" es el primer código que se ejecuta
     * cuando se lanza la app.
     */
    override fun onCreate() {
        super.onCreate()
        // Creamos la instancia única de SessionManager,
        // pasándole el "Contexto" de la aplicación (this).
        sessionManager = SessionManager(this)
    }
}