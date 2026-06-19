package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(
        primary = Color.White,
        onPrimary = DeepBlack,
        secondary = LightGrey,
        onSecondary = DeepBlack,
        background = DeepBlack,
        onBackground = Color.White,
        surface = Gunmetal,
        onSurface = Color.White,
        surfaceVariant = SlateGrey,
        onSurfaceVariant = LightGrey
    )

private val LightColorScheme =
    darkColorScheme( // We use a dark theme even for "light" for that premium dashboard look
        primary = Color.White,
        onPrimary = DeepBlack,
        secondary = LightGrey,
        background = DarkGrey,
        surface = Gunmetal
    )

@Composable
fun WeatherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disable for more consistent branding
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
