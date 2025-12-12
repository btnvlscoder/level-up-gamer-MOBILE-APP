package com.example.levelupgamermobile

import android.app.Application
import com.example.levelupgamermobile.data.AppContainer
import com.example.levelupgamermobile.data.DefaultAppContainer

/**
 * Clase de Aplicación personalizada.
 * Se inicia antes que cualquier Activity.
 */
class LevelUpGamerApp : Application() {

    lateinit var container: AppContainer

    /**
     * "onCreate" es el primer código que se ejecuta
     * cuando se lanza la app.
     */
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}