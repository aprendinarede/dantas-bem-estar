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

private val DarkColorScheme =
  darkColorScheme(
    primary = SagePrimaryDark,
    onPrimary = SageOnPrimaryDark,
    primaryContainer = SagePrimaryContainerDark,
    onPrimaryContainer = SageOnPrimaryContainerDark,
    secondary = ClaySecondaryDark,
    onSecondary = ClayOnSecondaryDark,
    secondaryContainer = ClaySecondaryContainerDark,
    onSecondaryContainer = ClayOnSecondaryContainerDark,
    tertiary = SereneTertiaryDark,
    onTertiary = SereneOnTertiaryDark,
    tertiaryContainer = SereneTertiaryContainerDark,
    onTertiaryContainer = SereneOnTertiaryContainerDark,
    background = WarmBackgroundDark,
    surface = WarmSurfaceDark,
    surfaceVariant = WarmSurfaceVariantDark,
    onBackground = WarmOnSurfaceDark,
    onSurface = WarmOnSurfaceDark,
    onSurfaceVariant = WarmOnSurfaceVariantDark
  )

private val LightColorScheme =
  lightColorScheme(
    primary = SagePrimary,
    onPrimary = SageOnPrimary,
    primaryContainer = SagePrimaryContainer,
    onPrimaryContainer = SageOnPrimaryContainer,
    secondary = ClaySecondary,
    onSecondary = ClayOnSecondary,
    secondaryContainer = ClaySecondaryContainer,
    onSecondaryContainer = ClayOnSecondaryContainer,
    tertiary = SereneTertiary,
    onTertiary = SereneOnTertiary,
    tertiaryContainer = SereneTertiaryContainer,
    onTertiaryContainer = SereneOnTertiaryContainer,
    background = WarmBackgroundLight,
    surface = WarmSurfaceLight,
    surfaceVariant = WarmSurfaceVariantLight,
    onBackground = WarmOnSurfaceLight,
    onSurface = WarmOnSurfaceLight,
    onSurfaceVariant = WarmOnSurfaceVariantLight
  )


@Composable
fun EloPsiTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Preserve intentional therapeutic color scheme
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

// Backwards compatibility alias
@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  EloPsiTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
