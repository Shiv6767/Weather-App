package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val CandyColorScheme = lightColorScheme(
    primary = CandyPrimary,
    onPrimary = CandyOnPrimary,
    primaryContainer = CandyPrimaryContainer,
    secondary = CandySecondary,
    onSecondary = CandyOnSecondary,
    secondaryContainer = CandySecondaryContainer,
    tertiary = CandyTertiary,
    onTertiary = CandyOnTertiary,
    tertiaryContainer = CandyTertiaryContainer,
    background = CandyBackground,
    onBackground = CandyOnSurface,
    surface = CandySurface,
    onSurface = CandyOnSurface,
    surfaceVariant = CandySurfaceContainer,
    onSurfaceVariant = CandyOnSurfaceVariant,
    outline = CandyOutline,
    outlineVariant = CandyOutlineVariant,
    error = CandyError,
    onError = CandyOnError,
    errorContainer = CandyErrorContainer,
    onErrorContainer = CandyOnErrorContainer
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled to enforce Candy brand colors
    content: @Composable () -> Unit,
) {
    val colorScheme = CandyColorScheme // Using light theme only as requested by design

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
