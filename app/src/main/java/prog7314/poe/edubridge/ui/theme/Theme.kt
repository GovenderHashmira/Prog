package prog7314.poe.edubridge.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = Navy,
    onPrimary = Color.White,
    primaryContainer = NavyContainer,
    onPrimaryContainer = OnNavyContainer,
    secondary = NavyLight,
    onSecondary = Color.White,
    tertiary = Accent,
    onTertiary = Color.White,
    tertiaryContainer = AccentContainer,
    onTertiaryContainer = OnAccentContainer,
    background = AppBackground,
    onBackground = OnNavyContainer,
    surface = AppSurface,
    onSurface = OnNavyContainer,
    surfaceVariant = AppSurfaceVariant,
    onSurfaceVariant = OnSurfaceMuted,
    outline = OutlineColor,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorContainer,
    onErrorContainer = ErrorRed
)

private val DarkColors = darkColorScheme(
    primary = NavyLight,
    onPrimary = Color.White,
    primaryContainer = NavyDark,
    onPrimaryContainer = NavyContainer,
    secondary = NavyContainer,
    onSecondary = NavyDark,
    tertiary = Accent,
    onTertiary = Color.White,
    tertiaryContainer = OnAccentContainer,
    onTertiaryContainer = AccentContainer,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceMuted,
    outline = OnSurfaceMuted,
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC)
)

@Composable
fun EduBridgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = EduBridgeTypography,
        content = content
    )
}
