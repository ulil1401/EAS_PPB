package com.coffeebliss.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val CoffeeGreen = Color(0xFF1B5E20)
val CoffeeGreenDark = Color(0xFF0D3B12)
val CoffeeGreenLight = Color(0xFF4CAF50)
val CoffeeBackground = Color(0xFFF5F5F5)
val CoffeeCardDark = Color(0xFF2E2E2E)

private val LightColorScheme = lightColorScheme(
    primary = CoffeeGreen,
    onPrimary = Color.White,
    primaryContainer = CoffeeGreenLight,
    onPrimaryContainer = Color.White,
    secondary = CoffeeGreenLight,
    onSecondary = Color.White,
    background = CoffeeBackground,
    onBackground = Color(0xFF212121),
    surface = Color.White,
    onSurface = Color(0xFF212121),
    tertiary = CoffeeGreenDark,
    onTertiary = Color.White
)

@Composable
fun CoffeeBlissTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
