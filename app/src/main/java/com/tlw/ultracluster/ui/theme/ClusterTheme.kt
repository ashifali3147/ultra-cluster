package com.tlw.ultracluster.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
fun ClusterTheme(isNight: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
}