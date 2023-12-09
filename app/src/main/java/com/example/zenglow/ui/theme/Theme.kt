/*
 FILE: Theme.kt
 AUTHOR: Nikolas Nosál <xnosal01>
 DESCRIPTION: Application dark/light themes
*/
@file:Suppress("DEPRECATION")

package com.example.zenglow.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController


/*
    COLOR SCHEME - WHITE
    App_Colors:
    UpperBar 	    = #FFFFFF
    LowerBar 	    = #ECECEC

    Text_Colors:
    TextBlack 	    = #000000
    TextSubBlack 	= #49454F
    TextWhite	    = #FFFFFF

    Background_Colors:
    BodyBackground 	= #ECECEC
    BodyTitle	    = #FFFFFF
    BodyListCard	= #FFFFFF
    BodyDialog	    = #DEDEDE

    /* Default Material3 colors */
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
 */


/* Dark theme */
private val DarkColorScheme = darkColorScheme(
    primary = Purple40,                     // Button On, Slider, Button Color [applies to material3]
    onPrimary = TextWhite,                  // Additional text
    primaryContainer = BodyBlackGrey,       // BodyElement
    onPrimaryContainer = TextWhite,         // BodyElementText
    inversePrimary = TextBlack,             // Special primary text

    secondaryContainer = Purple120,         // Selected button
    onSecondaryContainer = TextWhite,       // Selected button text

    surface = BodyBlackGrey,                // TitleBar, ListItems, Modals, NotificationBar
    onSurface = TextWhite,                  // TitleBarText, ListItemText, BackgroundText,
    inverseSurface = BodyBlack,             // Background, BottomNavBar,

    outline = OutlineGrey,                 // Button Outline, TextField Outline, Dividers
)

/* Light theme */
private val LightColorScheme = lightColorScheme(
    primary = Purple40,                     // Button On, Slider, Button Color [applies to material3]
    onPrimary = TextBlack,                  // Additional text
    primaryContainer = BodyWhite,           // BodyElement
    onPrimaryContainer = TextBlack,         // BodyElementText
    inversePrimary = TextWhite,             // Special primary text

    secondaryContainer = Purple80,          // Selected button
    onSecondaryContainer = TextBlack,       // Selected button text

    surface = BodyWhite,                    // TitleBar, ListItems, Modals, NotificationBar
    onSurface = TextBlack,                  // TitleBarText, ListItemText, BackgroundText,
    inverseSurface = BodyWhiteTinted,       // Background, BottomNavBar,

    outline = OutlineBlack,                 // Button Outline, TextField Outline, Dividers
)

@Composable
fun ZenGlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {

        val systemUiController = rememberSystemUiController()

        SideEffect {
            val window = (view.context as Activity).window

            // System notification bar
            systemUiController.setSystemBarsColor(
                color = colorScheme.surface,
                darkIcons = !darkTheme
            )

            // Status bar
            systemUiController.setNavigationBarColor(
                color = colorScheme.inverseSurface,
                darkIcons = !darkTheme
            )

            //window.statusBarColor = colorScheme.primary.toArgb()
            //window.navigationBarColor = Color.White.toArgb()

            //WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}