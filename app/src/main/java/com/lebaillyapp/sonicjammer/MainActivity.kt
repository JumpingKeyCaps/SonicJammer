package com.lebaillyapp.sonicjammer

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import com.lebaillyapp.sonicjammer.screen.JammerUIButtonPreview
import com.lebaillyapp.sonicjammer.screen.JammerUIScreen

import com.lebaillyapp.sonicjammer.ui.theme.SonicJammerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SonicJammerTheme {
                SetBarsColors(
                    statusBarColor = Color(0xFF0A0A0A),
                    navBarColor = Color(0xFF0A0A0A),
                )

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .padding(WindowInsets.systemBars.asPaddingValues()), // <-- évite l'overlap
                    contentAlignment = Alignment.TopCenter
                ) {
                    JammerUIButtonPreview()
                    // JammerUIScreen() // version finale
                }
            }
        }
    }
}

@Composable
fun SetBarsColors(
    statusBarColor: Color,
    navBarColor: Color
) {
    val window = (LocalView.current.context as Activity).window
    SideEffect {
        window.statusBarColor = statusBarColor.toArgb()
        window.navigationBarColor = navBarColor.toArgb()
    }
}



