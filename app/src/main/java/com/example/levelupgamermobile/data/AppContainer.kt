package com.example.levelupgamermobile.data

import android.content.Context
import androidx.room.Room
import com.example.levelupgamermobile.data.local.AppDatabase
import com.example.levelupgamermobile.data.repository.AuthRepository
import com.example.levelupgamermobile.data.repository.CarritoRepository
import com.example.levelupgamermobile.data.repository.ProductoRepository
import com.example.levelupgamermobile.data.repository.ResenaRepository
import com.example.levelupgamermobile.data.repository.VentaRepository
import com.example.levelupgamermobile.data.repository.TicketRepository
import com.example.levelupgamermobile.data.remote.RetrofitProvider

interface AppContainer {
    val productoRepository: ProductoRepository
    val carritoRepository: CarritoRepository
    val authRepository: AuthRepository
    val ventaRepository: VentaRepository
    val resenaRepository: ResenaRepository
    val ticketRepository: TicketRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "levelup_database"
        )
        .fallbackToDestructiveMigration() // Útil para desarrollo
        .build()
    }

    override val productoRepository: ProductoRepository by lazy {
        ProductoRepository(
            apiService = RetrofitProvider.apiService,
            productoDao = database.productoDao()
        )
    }

    override val carritoRepository: CarritoRepository by lazy {
        CarritoRepository(
            carritoDao = database.carritoDao()
        )
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepository(
            apiService = RetrofitProvider.apiService,
            usuarioDao = database.usuarioDao()
        )
    }

    override val ventaRepository: VentaRepository by lazy {
        VentaRepository(
            ventaDao = database.ventaDao()
        )
    }

    override val resenaRepository: ResenaRepository by lazy {
        ResenaRepository(
            resenaDao = database.resenaDao()
        )
    }

    override val ticketRepository: TicketRepository by lazy {
        TicketRepository(
            apiService = RetrofitProvider.apiService
        )
    }
}
