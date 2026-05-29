package best.mobile.stenik.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


/*
primary: Основной цвет бренда приложения.
secondary: Цвет для второстепенных элементов дизайна.
tertiary: Цвет для акцентирования и поддержки других цветов.
background: Цвет фона приложения.
surface: Цвет фона для элементов, таких как карточки и диалоги.
error: Цвет для отображения ошибок.
 */

val LightColorScheme = lightColorScheme(

    primary = MainColorW,
    onPrimary = MainColorTextW,
    surface = SurfaceW,
    surfaceContainer = SurfaceContW, //NavigationBar контейнер
    onSurfaceVariant = onSurfaceVariantW,//NavigationBar надписи и иконки
    secondaryContainer = secondaryContainerW,//NavigationBar фон вокруг выделенной иконки
    onSecondaryContainer = onSecondaryContainerW,//NavigationBar выделенная иконки
    onSurface = onSurfaceW,//NavigationBar выделенный текст
    //background = Red, //цвет фона
    //onBackground = Yellow // шрифт основной
/*
    primaryContainer = Yellow,
    onPrimaryContainer = Brown,
    inversePrimary = Green,
    secondary = Red,
    onSecondary = Yellow,
    tertiary = Red,
    onTertiary = Green,
    tertiaryContainer = Brown,
    onTertiaryContainer = Green,
    surfaceVariant = Red,
    surfaceTint = Brown,
    inverseSurface = Green,
    inverseOnSurface = Red,
    error = Yellow,
    onError = Brown,
    errorContainer = Green,
    onErrorContainer = Red,
    outline = Yellow,
    outlineVariant = Brown,
    scrim = Green,
    surfaceBright = Red,
    surfaceContainerHigh = Green,
    surfaceContainerHighest = Red,
    surfaceContainerLow = Yellow,
    surfaceContainerLowest = Brown,
    surfaceDim = Green,

 */

    )

private val DarkColorScheme = darkColorScheme(

    primary = MainColorB,
    onPrimary = MainColorTextB,
    surface = SurfaceB,
    surfaceContainer = SurfaceContB,
    onSurfaceVariant = onSurfaceVariantB,
    secondaryContainer = secondaryContainerB,
    onSecondaryContainer = onSecondaryContainerB,
    onSurface = onSurfaceB,







    )


@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
// Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {


        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
