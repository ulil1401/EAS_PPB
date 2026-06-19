package com.coffeebliss.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.coffeebliss.app.ui.theme.CoffeeGreen
import java.io.File

@Composable
fun ProfileAvatar(
    photoPath: String?,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(CoffeeGreen.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        if (!photoPath.isNullBlank() && File(photoPath).exists()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(File(photoPath))
                    .crossfade(true)
                    .build(),
                contentDescription = "Foto profil",
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(size * 0.5f),
                tint = CoffeeGreen
            )
        }
    }
}

private fun String?.isNullBlank(): Boolean = this.isNullOrBlank()
