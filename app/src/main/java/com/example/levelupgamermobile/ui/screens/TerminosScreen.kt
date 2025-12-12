package com.example.levelupgamermobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminosScreen(
    onBackPress: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Términos y Condiciones") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "1. Aceptación de los Términos",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    "Al descargar y utilizar la aplicación Level-Up Gamer, usted acepta estar sujeto a estos Términos y Condiciones. Si no está de acuerdo, no utilice la aplicación.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item {
                Text(
                    "2. Uso de la Cuenta",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    "Usted es responsable de mantener la confidencialidad de su cuenta y contraseña. Todas las actividades que ocurran bajo su cuenta son su responsabilidad. Nos reservamos el derecho de suspender o cancelar cuentas a nuestra discreción.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item {
                Text(
                    "3. Compras y Pagos",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    "Todos los precios están indicados en CLP (Pesos Chilenos). Los pagos se procesan a través de pasarelas de pago seguras. Level-Up Gamer no almacena la información de su tarjeta de crédito. Las compras son finales, excepto como se indique en nuestra política de devoluciones.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}