package com.example.levelupgamermobile.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.levelupgamermobile.ui.theme.LvlUpGreen
import kotlinx.coroutines.launch

/**
 * pantalla de "soporte" (formulario de contacto).
 *
 * esta pantalla es una maqueta (mockup) y no esta conectada
 * a un viewmodel.
 *
 * gestiona el estado de los campos de texto localmente
 * usando 'remember { mutablestateof(...) }'.
 *
 * al enviar, solo muestra un [Snackbar] de confirmacion
 * y limpia los campos del formulario.
 *
 * @param onBackPress funcion lambda para manejar la navegacion hacia atras.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoporteScreen(
    onBackPress: () -> Unit
) {
    // 1. estado local para los campos del formulario
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") } // corregido de 'correo'
    var asunto by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    // 2. estado para el snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("soporte y contacto") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // 3. 'lazycolumn' se usa para que el formulario
        //    sea 'scrollable' en pantallas pequenas.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(
                    "contactanos",
                    style = MaterialTheme.typography.titleLarge,
                    color = LvlUpGreen // color verde neon
                )
            }

            // --- formulario ---
            item {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("nombre completo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            item {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("correo electronico") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            item {
                OutlinedTextField(
                    value = asunto,
                    onValueChange = { asunto = it },
                    label = { Text("asunto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            item {
                OutlinedTextField(
                    value = mensaje,
                    onValueChange = { mensaje = it },
                    label = { Text("mensaje") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 5
                )
            }
            item {
                Button(
                    onClick = {
                        // 4. logica de maqueta (mock):
                        //    muestra el snackbar y limpia los campos.
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "mensaje enviado. ¡gracias por contactarnos!",
                                duration = SnackbarDuration.Short
                            )
                        }
                        nombre = ""
                        email = ""
                        asunto = ""
                        mensaje = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ENVIAR MENSAJE")
                }
            }

            // --- informacion de contacto estatica ---
            item { Spacer(Modifier.height(16.dp)) }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "informacion de contacto",
                            style = MaterialTheme.typography.titleMedium,
                            color = LvlUpGreen
                        )
                        InfoRow(
                            icon = Icons.Filled.Email,
                            text = "contacto@levelupgamer.cl"
                        )
                        InfoRow(
                            icon = Icons.Filled.Phone,
                            text = "+56 9 4129 5631"
                        )
                    }
                }
            }
        }
    }
}

/**
 * composable privado para dibujar una fila de informacion
 * (icono + texto).
 */
@Composable
private fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary // color azul
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}