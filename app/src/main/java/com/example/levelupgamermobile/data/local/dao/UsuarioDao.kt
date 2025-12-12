package com.example.levelupgamermobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.levelupgamermobile.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios LIMIT 1")
    fun obtenerUsuarioActual(): Flow<UsuarioEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarUsuario(usuario: UsuarioEntity)

    @Query("DELETE FROM usuarios")
    suspend fun borrarUsuario()
}
