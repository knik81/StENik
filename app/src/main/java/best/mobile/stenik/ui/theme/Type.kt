package best.mobile.stenik.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp


/*
display: Самые крупные стили, предназначенные для заголовков на главном экране или для выделения важной информации.
headline: Используются для заголовков разделов или экранов.
title: Применяются для заголовков внутри разделов, например, заголовков карточек.
body: Стандартный стиль основного текста, используемый для обычного контента.
label: Меньшие стили, используемые для подписей, кнопок, или тегов.
button: Специальный стиль для текста кнопок, обеспечивающий их читаемость при разных размерах
*/

// Set of Material typography styles to start with
val Typography = Typography(

    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 30.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),



    /* Other default text styles to override
`    titleLarge = TextStyle(
`        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
/*
     displayLarge = displayLarge,
            displayMedium = displayMedium,
            displaySmall = displaySmall,
            headlineLarge = headlineLarge,
            headlineMedium = headlineMedium,
            headlineSmall = headlineSmall,
            titleLarge = titleLarge,
            titleMedium = titleMedium,
            titleSmall = titleSmall,
            bodyLarge = bodyLarge,
            bodyMedium = bodyMedium,
            bodySmall = bodySmall,
            labelLarge = labelLarge,
            labelMedium = labelMedium,
            labelSmall = labelSmall


 */
)


