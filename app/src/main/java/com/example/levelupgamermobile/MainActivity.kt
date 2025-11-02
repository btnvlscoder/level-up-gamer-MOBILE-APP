// Asegúrate que el paquete sea el correcto
package com.example.levelupgamermobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

// IMPORT DEL TEMA Y PANTALLA
//
import com.example.levelupgamermobile.ui.theme.LevelUpGamerMobileTheme
import com.example.levelupgamermobile.view.ProductListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent define la UI
        setContent {
            // LLAMAMOS AL TEMA
            LevelUpGamerMobileTheme {

                // Surface es un contenedor básico de Material Design
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // LLAMAMOS A LA PANTALLA DE PRODUCTOS
                    ProductListScreen()
                }
            }
        }
    }
}