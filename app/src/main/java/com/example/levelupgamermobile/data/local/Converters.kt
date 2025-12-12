package com.example.levelupgamermobile.data.local

import androidx.room.TypeConverter
import com.example.levelupgamermobile.data.local.entity.DetalleVenta
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromDetalleVentaList(value: List<DetalleVenta>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDetalleVentaList(value: String): List<DetalleVenta> {
        val listType = object : TypeToken<List<DetalleVenta>>() {}.type
        return gson.fromJson(value, listType)
    }
}
