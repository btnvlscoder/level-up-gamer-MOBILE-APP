package com.example.levelupgamermobile.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * pantalla estatica que muestra los terminos y condiciones.
 *
 * esta pantalla es una vista de "solo texto". se accede a ella
 * desde el [ProfileMenuScreen].
 *
 * @param onBackPress funcion lambda para manejar la navegacion
 * hacia atras (volver al [ProfileMenuScreen]).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminosScreen(
    onBackPress: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("terminos y condiciones") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        // correccion: parametros 'imagevector' y 'contentdescription'
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // lazycolumn se usa para asegurar que el texto sea 'scrollable'
        // en pantallas pequenas.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // item 1: aceptacion
            item {
                Text(
                    "1. aceptacion de los terminos",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    "al descargar y utilizar la aplicacion level-up gamer, usted acepta estar sujeto a estos terminos y condiciones. si no esta de acuerdo, no utilice la aplicacion.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // item 2: uso de la cuenta
            item {
                Text(
                    "2. uso de la cuenta",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    "usted es responsable de mantener la confidencialidad de su cuenta y contrasena. todas las actividades que ocurran bajo su cuenta son su responsabilidad. nos reservamos el derecho de suspender o cancelar cuentas a nuestra discrecion.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // item 3: compras
            item {
                Text(
                    "3. compras y pagos",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    "todos los precios estan indicados en clp (pesos chilenos). los pagos se procesan a traves de pasarelas de pago seguras. level-up gamer no almacena la informacion de su tarjeta de credito. las compras son finales, excepto como se indique en nuestra politica de devoluciones.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // (puedes anadir mas items de texto aqui)
        }
    }
}