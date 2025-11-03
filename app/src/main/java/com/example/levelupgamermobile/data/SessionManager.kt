package com.example.levelupgamermobile.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.levelupgamermobile.model.UsuarioDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// (1) Esto crea el "archivo" físico de preferencias
// Se llamará "user_session.preferences_pb"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

/**
 * Esta clase maneja la lectura y escritura en DataStore.
 * Necesita un "Context" para saber dónde guardar el archivo.
 * (Recuerda: la creamos en LevelUpGamerApp.kt)
 */
class SessionManager(private val context: Context) {

    // (2) Definimos las "llaves" (claves) para nuestros datos.
    // Son como los nombres de las columnas en una base de datos.
    companion object {
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_RUT_KEY = stringPreferencesKey("user_rut")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
    }

    /**
     * Guarda los datos del usuario en DataStore.
     * Esta es una "suspend fun" porque la escritura
     * es asíncrona (no bloquea la UI).
     */
    suspend fun saveSession(usuario: UsuarioDTO) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = usuario.email
            preferences[USER_NAME_KEY] = usuario.nombre
            preferences[USER_RUT_KEY] = usuario.rut
            preferences[USER_ROLE_KEY] = usuario.rol ?: "" // Guarda el rol o vacío
        }
    }

    /**
     * (3) Lee el email del usuario.
     * Usamos un "Flow", que es un "stream" de datos.
     * DataStore nos avisará automáticamente si el email cambia.
     * Lo usaremos para el "auto-login".
     */
    val userEmailFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_EMAIL_KEY]
        }

    val userNameFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME_KEY]
        }

    val userRutFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_RUT_KEY]
        }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}