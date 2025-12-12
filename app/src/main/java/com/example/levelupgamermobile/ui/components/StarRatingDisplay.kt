package com.example.levelupgamermobile.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingDisplay(
    rating: Float,
    maxStars: Int = 5,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        repeat(maxStars) { index ->
            val isSelected = index < rating
            val tint = if (isSelected) {
                Color(0xFFFFC107) // Amber color for stars
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            }
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
