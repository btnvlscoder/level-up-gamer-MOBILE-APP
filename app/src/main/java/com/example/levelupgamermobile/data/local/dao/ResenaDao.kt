package com.example.levelupgamermobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.levelupgamermobile.data.local.entity.ResenaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResenaDao {
    @Query("SELECT * FROM resenas WHERE productId = :productId ORDER BY fecha DESC")
    fun getReviewsByProduct(productId: String): Flow<List<ResenaEntity>>

    @Query("SELECT * FROM resenas WHERE userEmail = :userEmail ORDER BY fecha DESC")
    fun getReviewsByUser(userEmail: String): Flow<List<ResenaEntity>>

    @Query("SELECT * FROM resenas WHERE userEmail = :userEmail AND productId = :productId LIMIT 1")
    suspend fun getReviewByUserAndProduct(userEmail: String, productId: String): ResenaEntity?

    @Query("DELETE FROM resenas WHERE userEmail = :userEmail")
    suspend fun deleteReviewsByUser(userEmail: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(resena: ResenaEntity)
}
