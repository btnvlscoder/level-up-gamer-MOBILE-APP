package com.example.levelupgamermobile.utils

import java.text.NumberFormat
import java.util.Date
import java.util.Locale

/**
 * archivo de utilidades para formatear tipos de datos.
 *
 * este archivo centraliza funciones de formato reutilizables
 * (ej. moneda, fecha) para evitar la duplicacion de codigo
 * en multiples vistas (views) y viewmodels.
 */

/**
 * formatea un valor int a un string de moneda
 * en formato de peso chileno (clp), sin decimales.
 *
 * @param price el precio como un int.
 * @return un string formateado (ej. "$29.990").
 */
fun formatPrice(price: Int): String {
    // obtiene la instancia de formato de moneda para el 'locale' (region)
    // especifico de chile (es-cl).
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    // asegura que no se muestren decimales (ej. ".00").
    format.maximumFractionDigits = 0
    return format.format(price)
}

/**
 * formatea un timestamp (long) a un string de fecha legible.
 *
 * @param timestamp la fecha en milisegundos (ej. system.currenttimemillis()).
 * @return un string formateado (ej. "04/11/2025 a las 18:30").
 */
fun formatDate(timestamp: Long): String {
    // define el patron de formato deseado (dia/mes/ano hora:minuto).
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm", Locale("es", "CL"))
    // crea un objeto 'date' desde el timestamp y lo formatea.
    return sdf.format(Date(timestamp))
}