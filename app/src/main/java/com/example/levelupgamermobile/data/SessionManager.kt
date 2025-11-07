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

// define la instancia de datastore para toda la app.
// el 'by preferencesdatastore' crea un singleton
// asociado al contexto de la aplicacion.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

/**
 * gestiona la persistencia de datos locales usando jetpack datastore.
 *
 * esta clase es responsable de:
 * - guardar y leer los datos de la sesion del usuario (ej. email, nombre).
 * - guardar y leer el historial de compras.
 * - serializar/deserializar listas complejas (como 'purchase') a json
 * para su almacenamiento.
 *
 * es instanciada como un singleton en la clase 'levelupgamerapp'
 * para que toda la app comparta una unica conexion a datastore.
 */
class SessionManager(private val context: Context) {

    // se usa gson para convertir listas de objetos (ej. historial de compras)
    // a un string json, ya que datastore solo guarda tipos primitivos.
    private val gson = Gson()

    // define las "llaves" (claves) unicas para cada dato
    // que guardamos en datastore.
    companion object {
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_APELLIDO_P_KEY = stringPreferencesKey("user_apellido_p")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")

        // llave para el historial de compras (guardado como json)
        private val PURCHASE_HISTORY_KEY = stringPreferencesKey("purchase_history")
    }

    /**
     * guarda los datos del usuario en datastore despues de un
     * login o registro exitoso.
     * es una funcion 'suspend' porque la escritura es asincrona.
     */
    suspend fun saveSession(usuario: UsuarioDTO) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = usuario.email
            preferences[USER_NAME_KEY] = usuario.nombre
            preferences[USER_APELLIDO_P_KEY] = usuario.apellidoPaterno
            preferences[USER_ROLE_KEY] = usuario.rol ?: ""
        }
    }

    // --- flujos (flows) de datos de la sesion ---
    // exponemos los datos como flujos (flows) para que los viewmodels
    // puedan observarlos y reaccionar a los cambios (ej. al hacer logout).

    val userEmailFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_EMAIL_KEY] }

    val userNameFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_NAME_KEY] }

    val userApellidoPFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_APELLIDO_P_KEY] }

    val userRolFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_ROLE_KEY] }

    /**
     * borra *todos* los datos de la sesion.
     * se llama durante el proceso de logout.
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // --- logica de historial de compras ---

    /**
     * expone un flujo (flow) del historial de compras.
     * lee el string json desde datastore y lo deserializa
     * (convierte) de nuevo a una 'list<purchase>'.
     *
     * si no hay historial, devuelve una lista json vacia "[]".
     */
    val purchaseHistoryFlow: Flow<List<Purchase>> = context.dataStore.data
        .map { preferences ->
            val jsonString = preferences[PURCHASE_HISTORY_KEY] ?: "[]"
            // typetoken es necesario para que gson entienda
            // que queremos deserializar a una lista de 'purchase'.
            val listType = object : TypeToken<List<Purchase>>() {}.type
            gson.fromJson<List<Purchase>>(jsonString, listType)
        }

    /**
     * guarda un nuevo 'voucher' (compra) en el historial.
     *
     * esta funcion:
     * 1. lee el historial actual (como json).
     * 2. lo deserializa a una lista mutable.
     * 3. anade la nueva compra a la lista.
     * 4. serializa la lista actualizada de nuevo a json.
     * 5. guarda el nuevo string json en datastore.
     */
    suspend fun savePurchase(items: List<CartItem>, total: Int) {
        // 1. crea el nuevo objeto de compra
        val newPurchase = Purchase(
            id = UUID.randomUUID().toString(),
            date = System.currentTimeMillis(), // guarda la fecha y hora actual
            items = items,
            total = total
        )

        // 2. lee el historial actual. 'first()' obtiene el valor mas reciente.
        val currentHistoryJson = context.dataStore.data.first()[PURCHASE_HISTORY_KEY] ?: "[]"
        val listType = object : TypeToken<MutableList<Purchase>>() {}.type
        val currentHistory: MutableList<Purchase> = gson.fromJson(currentHistoryJson, listType)

        // 3. anade la nueva compra al *principio* de la lista.
        currentHistory.add(0, newPurchase)

        // 4. convierte la lista actualizada a json.
        val newHistoryJson = gson.toJson(currentHistory)

        // 5. guarda el nuevo json.
        context.dataStore.edit { preferences ->
            preferences[PURCHASE_HISTORY_KEY] = newHistoryJson
        }
    }
}