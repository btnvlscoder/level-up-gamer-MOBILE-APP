package com.example.levelupgamermobile.data

import com.example.levelupgamermobile.R
import com.example.levelupgamermobile.model.Producto

object LocalProductDataSource {

    val productos = listOf(
        Producto(
            codigo = "JM001",
            categoria = "Juegos de Mesa",
            marca = "Devir",
            nombre = "Catan",
            precio = 29990,
            descripcion = "Un clásico juego de estrategia donde los jugadores compiten por colonizar y expandirse en la isla de Catan.",
            imagenes = listOf(
                R.drawable.catan_1,
                R.drawable.catan_2,
                R.drawable.catan_3
            )
        ),
        Producto(
            codigo = "JM002",
            categoria = "Juegos de Mesa",
            marca = "Devir",
            nombre = "Carcassonne",
            precio = 24990,
            descripcion = "Juego de colocación de fichas donde los jugadores construyen el paisaje medieval de Carcassonne.",
            imagenes = listOf(
                R.drawable.carcassonne_1,
                R.drawable.carcassonne_2,
                R.drawable.carcassonne_3
            )
        ),
        Producto(
            codigo = "AC001",
            categoria = "Accesorios",
            marca = "Microsoft",
            nombre = "Controlador Inalámbrico Xbox Series X",
            precio = 59990,
            descripcion = "Experiencia de juego cómoda con botones mapeables y respuesta táctil mejorada.",
            imagenes = listOf(
                R.drawable.controlador_inalambrico_xbox_series_x_1,
                R.drawable.controlador_inalambrico_xbox_series_x_2,
                R.drawable.controlador_inalambrico_xbox_series_x_3,
                R.drawable.controlador_inalambrico_xbox_series_x_4,
                R.drawable.controlador_inalambrico_xbox_series_x_5
            )
        ),
        Producto(
            codigo = "AC002",
            categoria = "Accesorios",
            marca = "HyperX",
            nombre = "Auriculares Gamer HyperX Cloud II",
            precio = 79990,
            descripcion = "Sonido envolvente de calidad con micrófono desmontable y almohadillas ultra cómodas.",
            imagenes = listOf(
                R.drawable.auriculares_gaming_hyperx_1,
                R.drawable.auriculares_gaming_hyperx_2,
                R.drawable.auriculares_gaming_hyperx_3,
                R.drawable.auriculares_gaming_hyperx_4
            )
        ),
        Producto(
            codigo = "CO001",
            categoria = "Consolas",
            marca = "Sony",
            nombre = "PlayStation 5",
            precio = 549990,
            descripcion = "Consola de última generación de Sony con gráficos impresionantes y carga ultrarrápida.",
            imagenes = listOf(
                R.drawable.playstation_5_1,
                R.drawable.playstation_5_2,
                R.drawable.playstation_5_3,
                R.drawable.playstation_5_4
            )
        ),
        Producto(
            codigo = "CG001",
            categoria = "Computadores Gamers",
            marca = "ASUS",
            nombre = "PC Gamer ASUS ROG Strix",
            precio = 1299990,
            descripcion = "Potente equipo diseñado para gamers exigentes, con rendimiento excepcional en cualquier juego.",
            imagenes = listOf(
                R.drawable.pc_gamer_asus_rog_strix_1,
                R.drawable.pc_gamer_asus_rog_strix_2,
                R.drawable.pc_gamer_asus_rog_strix_3,
                R.drawable.pc_gamer_asus_rog_strix_4
            )
        ),
        Producto(
            codigo = "SG001",
            categoria = "Sillas Gamers",
            marca = "Secretlab",
            nombre = "Silla Gamer Secretlab Titan",
            precio = 349990,
            descripcion = "Máximo confort y soporte ergonómico para sesiones largas de juego.",
            imagenes = listOf(
                R.drawable.silla_gamersecretlab_titan_1,
                R.drawable.silla_gamersecretlab_titan_2,
                R.drawable.silla_gamersecretlab_titan_3,
                R.drawable.silla_gamersecretlab_titan_4,
                R.drawable.silla_gamersecretlab_titan_5,
                R.drawable.silla_gamersecretlab_titan_6,
                R.drawable.silla_gamersecretlab_titan_7,
                R.drawable.silla_gamersecretlab_titan_8,
                R.drawable.silla_gamersecretlab_titan_9
            )
        ),
        Producto(
            codigo = "MS001",
            categoria = "Mouse",
            marca = "Logitech",
            nombre = "Mouse Gamer Logitech G502 HERO",
            precio = 49990,
            descripcion = "Sensor de alta precisión y botones personalizables para un control preciso.",
            imagenes = listOf(
                R.drawable.mouse_gamer_logitech_g502_hero_1,
                R.drawable.mouse_gamer_logitech_g502_hero_2,
                R.drawable.mouse_gamer_logitech_g502_hero_3,
                R.drawable.mouse_gamer_logitech_g502_hero_4
            )
        ),
        Producto(
            codigo = "MP001",
            categoria = "Mousepad",
            marca = "Razer",
            nombre = "Mousepad Razer Goliathus Extended Chroma",
            precio = 29990,
            descripcion = "Área amplia de juego con iluminación RGB personalizable.",
            imagenes = listOf(
                R.drawable.mousepad_razer_goliathus_extended_chroma_1,
                R.drawable.mousepad_razer_goliathus_extended_chroma_2,
                R.drawable.mousepad_razer_goliathus_extended_chroma_3
            )
        ),
        Producto(
            codigo = "PP001",
            categoria = "Poleras Personalizadas",
            marca = "Level-Up",
            nombre = "Polera Gamer Personalizada 'Level-Up'",
            precio = 14990,
            descripcion = "Camiseta cómoda y estilizada, personalizable con tu gamer tag o diseño favorito.",
            imagenes = listOf(
                R.drawable.poleras_personalizadas_1,
                R.drawable.poleras_personalizadas_2,
                R.drawable.poleras_personalizadas_3
            )
        )
    )
}