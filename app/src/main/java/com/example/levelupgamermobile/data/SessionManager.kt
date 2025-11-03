package com.example.levelupgamermobile.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.levelupgamermobile.model.UsuarioDTO // ¡Importante!
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    // (1) ¡NUEVAS CLAVES!
    companion object {
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_RUT_KEY = stringPreferencesKey("user_rut")
        private val USER_APELLIDO_P_KEY = stringPreferencesKey("user_apellido_p") // ¡NUEVO!
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
    }

    /**
     * (2) ¡CAMBIO! Ahora guarda el objeto UsuarioDTO completo.
     */
    suspend fun saveSession(usuario: UsuarioDTO) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = usuario.email
            preferences[USER_NAME_KEY] = usuario.nombre
            preferences[USER_RUT_KEY] = usuario.rut
            preferences[USER_APELLIDO_P_KEY] = usuario.apellidoPaterno // ¡NUEVO!
            preferences[USER_ROLE_KEY] = usuario.rol ?: "" // ¡NUEVO!
        }
    }

    // (3) Flujos de datos (Streams)
    val userEmailFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_EMAIL_KEY] }

    val userNameFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_NAME_KEY] }

    val userRutFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_RUT_KEY] }

    // (4) ¡NUEVOS FLUJOS!
    val userApellidoPFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_APELLIDO_P_KEY] }

    val userRolFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_ROLE_KEY] }

    /**
     * Borra todos los datos de la sesión (para el Logout).
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}