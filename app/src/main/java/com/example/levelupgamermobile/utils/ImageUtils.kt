package com.example.levelupgamermobile.utils

import com.example.levelupgamermobile.R

/**
 * Mapea códigos de producto a recursos de imagen locales.
 * Esto asegura que siempre se usen los IDs de recursos correctos para la compilación actual,
 * evitando problemas con IDs obsoletos almacenados en la base de datos.
 */
fun getImageResForProduct(codigo: String): Int {
    return when (codigo) {
        "CO001" -> R.drawable.playstation_5_1
        "AC002" -> R.drawable.auriculares_gaming_hyperx_1
        "MS001" -> R.drawable.mouse_gamer_logitech_g502_hero_1
        "JM001" -> R.drawable.catan_1
        "JM002" -> R.drawable.carcassonne_1
        "MP001" -> R.drawable.mousepad_razer_goliathus_extended_chroma_1
        "CG001" -> R.drawable.pc_gamer_asus_rog_strix_1
        "SG001" -> R.drawable.silla_gamersecretlab_titan_1
        "AC001" -> R.drawable.controlador_inalambrico_xbox_series_x_1
        "PP001" -> R.drawable.poleras_personalizadas_1
        else -> R.drawable.placeholder_image
    }
}
