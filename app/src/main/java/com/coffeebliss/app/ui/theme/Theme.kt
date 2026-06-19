package com.coffeebliss.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CoffeeBrown = Color(0xFF6F4E37)
private val CoffeeBrownDark = Color(0xFF4A3222)
private val CoffeeCream = Color(0xFFFFF8F0)
private val CoffeeGold = Color(0xFFD4A574)
private val CoffeeGreen = Color(0xFF2E7D32)

private val LightColorScheme = lightColorScheme(
    primary = CoffeeBrown,
    onPrimary = Color.White,
    primaryContainer = CoffeeGold,
    onPrimaryContainer = CoffeeBrownDark,
    secondary = CoffeeGold,
    onSecondary = CoffeeBrownDark,
    background = CoffeeCream,
    onBackground = CoffeeBrownDark,
    surface = Color.White,
    onSurface = CoffeeBrownDark,
    tertiary = CoffeeGreen,
    onTertiary = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = CoffeeGold,
    onPrimary = CoffeeBrownDark,
    primaryContainer = CoffeeBrown,
    onPrimaryContainer = CoffeeCream,
    secondary = CoffeeGold,
    onSecondary = CoffeeBrownDark,
    background = Color(0xFF1A120B),
    onBackground = CoffeeCream,
    surface = Color(0xFF2A1F17),
    onSurface = CoffeeCream,
    tertiary = CoffeeGreen,
    onTertiary = Color.White
)

@Composable
fun CoffeeBlissTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
