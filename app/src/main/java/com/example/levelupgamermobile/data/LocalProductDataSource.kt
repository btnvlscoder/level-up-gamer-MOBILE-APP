package com.example.levelupgamermobile.data

import com.example.levelupgamermobile.R
import com.example.levelupgamermobile.model.Product

/**
 * simula una base de datos local (o un endpoint de api)
 * para el catalogo de productos.
 *
 * se implementa como un 'object' (singleton) para que la lista
 * de productos se cargue en memoria una sola vez.
 *
 * el ProductRepository consume esta lista.
 *
 * nota: las referencias 'r.drawable' requieren que las imagenes
 * con esos nombres (ej. 'catan_1.png') existan en la carpeta 'res/drawable'.
 */
object LocalProductDataSource {

    val products = listOf(
        Product(
            codigo = "JM001",
            categoria = "Juegos de mesa",
            marca = "Devir",
            nombre = "Catan",
            precio = 29990,
            descripcion = "Un clasico juego de estrategia donde los jugadores compiten por colonizar y expandirse en la isla de catan.",
            imagenes = listOf(
                R.drawable.catan_1,
                R.drawable.catan_2,
                R.drawable.catan_3
            )
        ),
        Product(
            codigo = "JM002",
            categoria = "Juegos de mesa",
            marca = "Devir",
            nombre = "Carcassonne",
            precio = 24990,
            descripcion = "Juego de colocacion de fichas donde los jugadores construyen el paisaje medieval de carcassonne.",
            imagenes = listOf(
                R.drawable.carcassonne_1,
                R.drawable.carcassonne_2,
                R.drawable.carcassonne_3
            )
        ),
        Product(
            codigo = "AC001",
            categoria = "Accesorios",
            marca = "Microsoft",
            nombre = "Controlador inalambrico xbox series x",
            precio = 59990,
            descripcion = "Experiencia de juego comoda con botones mapeables y respuesta tactil mejorada.",
            imagenes = listOf(
                R.drawable.controlador_inalambrico_xbox_series_x_1,
                R.drawable.controlador_inalambrico_xbox_series_x_2,
                R.drawable.controlador_inalambrico_xbox_series_x_3,
                R.drawable.controlador_inalambrico_xbox_series_x_4,
                R.drawable.controlador_inalambrico_xbox_series_x_5
            )
        ),
        Product(
            codigo = "AC002",
            categoria = "Accesorios",
            marca = "Hyperx",
            nombre = "Auriculares gamer hyperx cloud ii",
            precio = 79990,
            descripcion = "Sonido envolvente de calidad con microfono desmontable y almohadillas ultra comodas.",
            imagenes = listOf(
                R.drawable.auriculares_gaming_hyperx_1,
                R.drawable.auriculares_gaming_hyperx_2,
                R.drawable.auriculares_gaming_hyperx_3,
                R.drawable.auriculares_gaming_hyperx_4
            )
        ),
        Product(
            codigo = "CO001",
            categoria = "Consolas",
            marca = "Sony",
            nombre = "Playstation 5",
            precio = 549990,
            descripcion = "Consola de ultima generacion de sony con graficos impresionantes y carga ultrarrapida.",
            imagenes = listOf(
                R.drawable.playstation_5_1,
                R.drawable.playstation_5_2,
                R.drawable.playstation_5_3,
                R.drawable.playstation_5_4
            )
        ),
        Product(
            codigo = "CG001",
            categoria = "Computadores gamers",
            marca = "Asus",
            nombre = "Pc gamer asus rog strix",
            precio = 1299990,
            descripcion = "Potente equipo disenaado para gamers exigentes, con rendimiento excepcional en cualquier juego.",
            imagenes = listOf(
                R.drawable.pc_gamer_asus_rog_strix_1,
                R.drawable.pc_gamer_asus_rog_strix_2,
                R.drawable.pc_gamer_asus_rog_strix_3,
                R.drawable.pc_gamer_asus_rog_strix_4
            )
        ),
        Product(
            codigo = "SG001",
            categoria = "Sillas gamers",
            marca = "Secretlab",
            nombre = "Silla gamer secretlab titan",
            precio = 349990,
            descripcion = "Maximo confort y soporte ergonomico para sesiones largas de juego.",
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
        Product(
            codigo = "MS001",
            categoria = "Mouse",
            marca = "Logitech",
            nombre = "Mouse gamer logitech g502 hero",
            precio = 49990,
            descripcion = "Sensor de alta precision y botones personalizables para un control preciso.",
            imagenes = listOf(
                R.drawable.mouse_gamer_logitech_g502_hero_1,
                R.drawable.mouse_gamer_logitech_g502_hero_2,
                R.drawable.mouse_gamer_logitech_g502_hero_3,
                R.drawable.mouse_gamer_logitech_g502_hero_4
            )
        ),
        Product(
            codigo = "MP001",
            categoria = "mousepad",
            marca = "Razer",
            nombre = "Mousepad razer goliathus extended chroma",
            precio = 29990,
            descripcion = "Area amplia de juego con iluminacion rgb personalizable.",
            imagenes = listOf(
                R.drawable.mousepad_razer_goliathus_extended_chroma_1,
                R.drawable.mousepad_razer_goliathus_extended_chroma_2,
                R.drawable.mousepad_razer_goliathus_extended_chroma_3
            )
        ),
        Product(
            codigo = "PP001",
            categoria = "poleras personalizadas",
            marca = "Level-up",
            nombre = "Polera gamer personalizada 'level-up'",
            precio = 14990,
            descripcion = "Camiseta comoda y estilizada, personalizable con tu gamer tag o diseno favorito.",
            imagenes = listOf(
                R.drawable.poleras_personalizadas_1,
                R.drawable.poleras_personalizadas_2,
                R.drawable.poleras_personalizadas_3
            )
        )
    )
}