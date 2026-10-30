package com.tlw.ultracluster.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalClusterColors = staticCompositionLocalOf { DayColors }

object ClusterTheme {
    val colors: ClusterColors
        @Composable @ReadOnlyComposable get() = LocalClusterColors.current
}

@Composable
fun ClusterTheme(isNight: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val color = if (isNight) NightColors else DayColors
    val material = if (isNight)
        darkColorScheme(
            primary = color.ride,
            background = color.bg,
            surface = color.panel,
            onSurface = color.ink
        )
    else
        lightColorScheme(
            primary = color.ride,
            background = color.bg,
            surface = color.panel,
            onSurface = color.ink
        )

    CompositionLocalProvider(LocalClusterColors provides color) {
        MaterialTheme(colorScheme = material) {
            ProvideTextStyle(ClusterType.body.copy(color = color.ink)) { content() }
        }
    }
}