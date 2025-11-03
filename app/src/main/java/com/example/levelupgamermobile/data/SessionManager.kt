package com.example.levelupgamermobile.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.levelupgamermobile.model.CartItem
import com.example.levelupgamermobile.model.Purchase
import com.example.levelupgamermobile.model.UsuarioDTO
import com.google.gson.Gson // ¡NUEVO IMPORT! Para JSON
import com.google.gson.reflect.TypeToken // ¡NUEVO IMPORT!
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    // (1) ¡NUEVO! Instancia de Gson para convertir objetos a JSON
    private val gson = Gson()

    companion object {
        // (Claves de sesión de usuario)
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_RUT_KEY = stringPreferencesKey("user_rut")
        private val USER_APELLIDO_P_KEY = stringPreferencesKey("user_apellido_p")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")

        // (2) ¡NUEVA CLAVE! Para guardar el historial
        private val PURCHASE_HISTORY_KEY = stringPreferencesKey("purchase_history")
    }

    // --- (Lógica de Sesión de Usuario - Sin cambios) ---
    suspend fun saveSession(usuario: UsuarioDTO) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = usuario.email
            preferences[USER_NAME_KEY] = usuario.nombre
            preferences[USER_RUT_KEY] = usuario.rut
            preferences[USER_APELLIDO_P_KEY] = usuario.apellidoPaterno
            preferences[USER_ROLE_KEY] = usuario.rol ?: ""
        }
    }

    val userEmailFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_EMAIL_KEY] }
    val userNameFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_NAME_KEY] }
    val userRutFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_RUT_KEY] }
    val userApellidoPFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_APELLIDO_P_KEY] }
    val userRolFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_ROLE_KEY] }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // --- (¡NUEVA!) LÓGICA DE HISTORIAL DE COMPRAS ---

    /**
     * (3) Lee el historial de compras.
     * Lee el string JSON y lo convierte de nuevo en una Lista de Compras.
     */
    val purchaseHistoryFlow: Flow<List<Purchase>> = context.dataStore.data
        .map { preferences ->
            val jsonString = preferences[PURCHASE_HISTORY_KEY] ?: "[]" // Valor por defecto: "[]" (lista vacía)
            // Define el "tipo" de lista que Gson debe crear
            val listType = object : TypeToken<List<Purchase>>() {}.type
            // Convierte el JSON en objetos
            gson.fromJson<List<Purchase>>(jsonString, listType)
        }

    /**
     * (4) Guarda una nueva compra.
     */
    suspend fun savePurchase(items: List<CartItem>, total: Int) {
        // (A) Crea el nuevo objeto de compra
        val newPurchase = Purchase(
            id = UUID.randomUUID().toString(),
            date = System.currentTimeMillis(), // Fecha y hora actual
            items = items,
            total = total
        )

        // (B) Obtiene la lista actual de compras (el JSON)
        val currentHistoryJson = context.dataStore.data.first()[PURCHASE_HISTORY_KEY] ?: "[]"
        val listType = object : TypeToken<MutableList<Purchase>>() {}.type
        val currentHistory: MutableList<Purchase> = gson.fromJson(currentHistoryJson, listType)

        // (C) Añade la nueva compra a la lista
        currentHistory.add(0, newPurchase) // Se añade al principio

        // (D) Convierte la lista actualizada de nuevo a JSON
        val newHistoryJson = gson.toJson(currentHistory)

        // (E) Guarda el nuevo JSON en DataStore
        context.dataStore.edit { preferences ->
            preferences[PURCHASE_HISTORY_KEY] = newHistoryJson
        }
    }
}