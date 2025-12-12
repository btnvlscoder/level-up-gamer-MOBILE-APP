package com.example.levelupgamermobile.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.levelupgamermobile.LevelUpGamerApp
import com.example.levelupgamermobile.ui.viewmodel.CartViewModel
import com.example.levelupgamermobile.ui.viewmodel.InfoPersonalViewModel
import com.example.levelupgamermobile.ui.viewmodel.LoginViewModel
import com.example.levelupgamermobile.ui.viewmodel.MisComprasViewModel
import com.example.levelupgamermobile.ui.viewmodel.MisOpinionesViewModel
import com.example.levelupgamermobile.ui.viewmodel.ProductDetailViewModel
import com.example.levelupgamermobile.ui.viewmodel.ProductoViewModel
import com.example.levelupgamermobile.ui.viewmodel.RegisterViewModel
import com.example.levelupgamermobile.ui.viewmodel.SoporteViewModel
import com.example.levelupgamermobile.ui.viewmodel.MisTicketsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ProductoViewModel
        initializer {
            ProductoViewModel(
                productoRepository = levelUpGamerApplication().container.productoRepository,
                carritoRepository = levelUpGamerApplication().container.carritoRepository
            )
        }

        // Initializer for ProductDetailViewModel
        initializer {
            ProductDetailViewModel(
                savedStateHandle = createSavedStateHandle(),
                productoRepository = levelUpGamerApplication().container.productoRepository,
                carritoRepository = levelUpGamerApplication().container.carritoRepository,
                resenaRepository = levelUpGamerApplication().container.resenaRepository,
                authRepository = levelUpGamerApplication().container.authRepository
            )
        }
        
        // Initializer for CartViewModel
        initializer {
            CartViewModel(
                carritoRepository = levelUpGamerApplication().container.carritoRepository,
                ventaRepository = levelUpGamerApplication().container.ventaRepository,
                authRepository = levelUpGamerApplication().container.authRepository
            )
        }

        // Initializer for LoginViewModel
        initializer {
            LoginViewModel(
                authRepository = levelUpGamerApplication().container.authRepository
            )
        }

        // Initializer for RegisterViewModel
        initializer {
            RegisterViewModel(
                authRepository = levelUpGamerApplication().container.authRepository
            )
        }

        // Initializer for InfoPersonalViewModel
        initializer {
            InfoPersonalViewModel(
                authRepository = levelUpGamerApplication().container.authRepository
            )
        }

        // Initializer for MisComprasViewModel
        initializer {
            MisComprasViewModel(
                ventaRepository = levelUpGamerApplication().container.ventaRepository
            )
        }

        // Initializer for MisOpinionesViewModel
        initializer {
            MisOpinionesViewModel(
                authRepository = levelUpGamerApplication().container.authRepository,
                resenaRepository = levelUpGamerApplication().container.resenaRepository,
                productoRepository = levelUpGamerApplication().container.productoRepository
            )
        }

        // Initializer for SoporteViewModel
        initializer {
            SoporteViewModel(
                authRepository = levelUpGamerApplication().container.authRepository,
                ticketRepository = levelUpGamerApplication().container.ticketRepository
            )
        }

        // Initializer for MisTicketsViewModel
        initializer {
            MisTicketsViewModel(
                levelUpGamerApplication().container.ticketRepository,
                levelUpGamerApplication().container.authRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [LevelUpGamerApp].
 */
fun CreationExtras.levelUpGamerApplication(): LevelUpGamerApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LevelUpGamerApp)
