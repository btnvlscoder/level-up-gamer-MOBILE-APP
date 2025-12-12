package com.example.levelupgamermobile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.levelupgamermobile.data.local.dao.CarritoDao
import com.example.levelupgamermobile.data.local.dao.ProductoDao
import com.example.levelupgamermobile.data.local.dao.ResenaDao
import com.example.levelupgamermobile.data.local.dao.UsuarioDao
import com.example.levelupgamermobile.data.local.dao.VentaDao
import com.example.levelupgamermobile.data.local.entity.CarritoItemEntity
import com.example.levelupgamermobile.data.local.entity.ProductoEntity
import com.example.levelupgamermobile.data.local.entity.ResenaEntity
import com.example.levelupgamermobile.data.local.entity.UsuarioEntity
import com.example.levelupgamermobile.data.local.entity.VentaEntity

@Database(
    entities = [
        ProductoEntity::class, 
        VentaEntity::class, 
        UsuarioEntity::class, 
        CarritoItemEntity::class,
        ResenaEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun ventaDao(): VentaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun carritoDao(): CarritoDao
    abstract fun resenaDao(): ResenaDao
}
