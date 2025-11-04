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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    private val gson = Gson()

    companion object {
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_APELLIDO_P_KEY = stringPreferencesKey("user_apellido_p")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val PURCHASE_HISTORY_KEY = stringPreferencesKey("purchase_history")
    }

    suspend fun saveSession(usuario: UsuarioDTO) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = usuario.email
            preferences[USER_NAME_KEY] = usuario.nombre
            // preferences[USER_RUT_KEY] = usuario.rut // <-- ELIMINADO
            preferences[USER_APELLIDO_P_KEY] = usuario.apellidoPaterno
            preferences[USER_ROLE_KEY] = usuario.rol ?: ""
        }
    }

    val userEmailFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_EMAIL_KEY] }

    val userNameFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_NAME_KEY] }

    // val userRutFlow: Flow<String?> = ... // <-- ELIMINADO

    val userApellidoPFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_APELLIDO_P_KEY] }

    val userRolFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_ROLE_KEY] }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // --- (Lógica de Historial de Compras - Sin cambios) ---
    val purchaseHistoryFlow: Flow<List<Purchase>> = context.dataStore.data
        .map { preferences ->
            val jsonString = preferences[PURCHASE_HISTORY_KEY] ?: "[]"
            val listType = object : TypeToken<List<Purchase>>() {}.type
            gson.fromJson<List<Purchase>>(jsonString, listType)
        }

    suspend fun savePurchase(items: List<CartItem>, total: Int) {
        val newPurchase = Purchase(
            id = UUID.randomUUID().toString(),
            date = System.currentTimeMillis(),
            items = items,
            total = total
        )
        val currentHistoryJson = context.dataStore.data.first()[PURCHASE_HISTORY_KEY] ?: "[]"
        val listType = object : TypeToken<MutableList<Purchase>>() {}.type
        val currentHistory: MutableList<Purchase> = gson.fromJson(currentHistoryJson, listType)
        currentHistory.add(0, newPurchase)
        val newHistoryJson = gson.toJson(currentHistory)
        context.dataStore.edit { preferences ->
            preferences[PURCHASE_HISTORY_KEY] = newHistoryJson
        }
    }
}