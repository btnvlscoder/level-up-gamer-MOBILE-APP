package com.example.levelupgamermobile

import android.app.Application
import com.example.levelupgamermobile.data.SessionManager

/**
 * clase de aplicacion personalizada para [LevelUpGamerApp].
 *
 * esta clase es el punto de entrada principal cuando se inicia
 * la aplicacion, incluso antes que [MainActivity].
 *
 * su proposito principal es inicializar y proveer
 * instancias singleton (unicas) que necesitan estar
 * disponibles en toda la app, como el [SessionManager].
 *
 * esta clase esta registrada en el 'androidmanifest.xml'.
 */
class LevelUpGamerApp : Application() {

    companion object {
        /**
         * la instancia unica (singleton) de [SessionManager].
         *
         * se usa 'lateinit' porque se inicializa en [onCreate].
         * 'private set' asegura que solo esta clase [LevelUpGamerApp]
         * pueda asignar su valor, pero el resto de la app
         * (ej. [AuthRepository]) pueda leerlo.
         */
        lateinit var sessionManager: SessionManager
            private set
    }

    /**
     * se llama cuando la aplicacion se inicia.
     *
     * este es el lugar ideal para inicializar
     * los singletons que dependen del contexto.
     */
    override fun onCreate() {
        super.onCreate()

        // inicializa la instancia unica de [SessionManager]
        // pasandole el contexto (this) de la aplicacion.
        sessionManager = SessionManager(this)
    }
}