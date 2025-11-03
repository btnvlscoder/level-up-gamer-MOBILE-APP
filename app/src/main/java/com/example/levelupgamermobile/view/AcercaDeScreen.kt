package com.example.levelupgamermobile.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.levelupgamermobile.R // ¡Importa R!

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcercaDeScreen(
    onBackPress: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Acerca de Level-Up Gamer") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Para que sea deslizable
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // (1) El Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Level-Up Gamer",
                modifier = Modifier
                    .size(200.dp)
                    .padding(vertical = 24.dp)
            )

            // (2) Texto coherente
            Text(
                "¡Tu tienda para subir de nivel!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "Level-Up Gamer es una aplicación demo creada con Jetpack Compose y Kotlin para la asignatura de Programación de Aplicaciones Móviles. Este proyecto simula una tienda de e-commerce completa, desde la autenticación de usuarios hasta un catálogo de productos y un carrito de compras funcional.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                "Desarrollado por:",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "Bastián Corona & Francisco Toloza",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}