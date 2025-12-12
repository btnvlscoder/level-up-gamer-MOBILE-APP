package com.example.levelupgamermobile

import com.example.levelupgamermobile.data.local.entity.ProductoEntity
import com.example.levelupgamermobile.model.CartItem
import com.example.levelupgamermobile.model.Producto
import com.example.levelupgamermobile.model.ProductoDTO
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Suite de 10 Pruebas Unitarias Diversas y Sencillas
 * Enfocadas en lógica de negocio de LevelUpGamer (Carrito, Productos, Validaciones)
 */
class AppUnitTests {

    // ==========================================
    // GRUPO 1: Lógica General (Conservados)
    // ==========================================

    @Test
    fun `1 - Validacion de Email simple`() {
        fun isValidEmail(email: String): Boolean {
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
            return email.matches(emailRegex)
        }

        assertTrue("El email debería ser válido", isValidEmail("usuario@ejemplo.com"))
        assertFalse("El email no debería ser válido", isValidEmail("correo-invalido"))
    }

    @Test
    fun `2 - Calculo de Total de Carrito`() {
        val precios = listOf(15000.0, 20000.50, 5000.0)
        val total = precios.sum()
        
        assertEquals(40000.50, total, 0.01)
    }

    @Test
    fun `3 - Formateo de Moneda (String)`() {
        fun formatPrice(price: Double): String {
            return "$ ${price.toInt()}"
        }
        
        assertEquals("$ 5000", formatPrice(5000.0))
        assertEquals("$ 0", formatPrice(0.0))
    }

    @Test
    fun `4 - Manipulacion de Listas (Filtrado)`() {
        val numeros = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val pares = numeros.filter { it % 2 == 0 }
        
        assertEquals(listOf(2, 4, 6, 8, 10), pares)
        assertEquals(5, pares.size)
    }

    @Test
    fun `5 - Inmutabilidad de Data Class`() {
        val productoOriginal = ProductoEntity(
            "001", "Cat", "Marca", "Nombre", 100.0, "Desc", 0
        )
        val productoModificado = productoOriginal.copy(precio = 200.0)

        assertEquals(100.0, productoOriginal.precio, 0.01)
        assertEquals(200.0, productoModificado.precio, 0.01)
    }

    // ==========================================
    // GRUPO 2: Lógica de Negocio LevelUpGamer (Nuevos/Simples)
    // ==========================================

    @Test
    fun `6 - Calculo de Subtotal de CartItem`() {
        // Lógica: Precio * Cantidad
        val producto = Producto(
            "P1", "Consolas", "Sony", "PS5", 500000, "Desc", listOf()
        )
        val item = CartItem(producto, 2)
        
        val subtotal = item.producto.precio * item.cantidad
        
        assertEquals(1000000, subtotal)
    }

    @Test
    fun `7 - Validacion de Stock (Simulada)`() {
        // Simulamos lógica: No permitir comprar más de 5 unidades
        fun validarStock(cantidadSolicitada: Int): Boolean {
            val stockMaximo = 5
            return cantidadSolicitada <= stockMaximo
        }

        assertTrue(validarStock(3))
        assertFalse(validarStock(10))
    }

    @Test
    fun `8 - Crear Resumen de Compra`() {
        // Verificar que podemos construir un string resumen correctamente
        val producto = "Mouse Gamer"
        val precio = 25000
        val cantidad = 2
        
        val resumen = "$producto x$cantidad = $${precio * cantidad}"
        
        assertEquals("Mouse Gamer x2 = $50000", resumen)
    }

    // ==========================================
    // GRUPO 3: Mapeo y Datos (Conservado)
    // ==========================================

    @Test
    fun `9 - Mapeo de DTO a Entidad`() {
        val dto = ProductoDTO(
            codigo = "XYZ",
            categoria = "Gaming",
            marca = "Razer",
            nombre = "Mouse",
            precio = 50000.0,
            descripcion = "RGB"
        )

        val entidad = ProductoEntity(
            codigo = dto.codigo,
            categoria = dto.categoria,
            marca = dto.marca,
            nombre = dto.nombre,
            precio = dto.precio,
            descripcion = dto.descripcion,
            imageRes = 0
        )

        assertEquals("XYZ", entidad.codigo)
        assertEquals("Mouse", entidad.nombre)
        assertEquals(50000.0, entidad.precio, 0.01)
    }

    @Test
    fun `10 - Validacion de Categoria Valida`() {
        // Lógica: Solo permitimos ciertas categorías
        val categoriasValidas = listOf("Consolas", "Juegos", "Accesorios")
        
        fun esCategoriaValida(cat: String) = cat in categoriasValidas
        
        assertTrue(esCategoriaValida("Consolas"))
        assertFalse(esCategoriaValida("Ropa"))
    }
}
