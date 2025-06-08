package com.lebaillyapp.sonicjammer.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lebaillyapp.sonicjammer.composition.ButtonRockerSwitchVertical
import com.lebaillyapp.sonicjammer.viewmodel.JamViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lebaillyapp.sonicjammer.R
import com.lebaillyapp.sonicjammer.composition.RepeatingRippleCanvas
import com.lebaillyapp.sonicjammer.composition.RepeatingRippleCyberCanvas
import com.lebaillyapp.sonicjammer.oldStuff.composable.afficheurs.DynamikRowAfficheur
import com.lebaillyapp.sonicjammer.oldStuff.config.SevenSegmentConfig
import com.lebaillyapp.sonicjammer.oldStuff.config.reflectConfig
import com.lebaillyapp.sonicjammer.oldStuff.iteratorConfigGenerator
import kotlin.math.sin

@Composable
fun JammerUIScreen(
    viewModel: JamViewModel = viewModel()
) {
    val isEmitting by viewModel.isPlaying.collectAsState()

    // UI principale
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ButtonRockerSwitchVertical(
            isChecked = isEmitting,
            onToggle = { shouldStart ->
                if (shouldStart) {
                    viewModel.startPlaying()
                } else {
                    viewModel.stopPlaying()
                }
            },
            iconSource = painterResource(R.drawable.emit) // Assure-toi que cette ressource existe
        )
    }
}




@Composable
fun JammerUIButtonPreview() {
    var isChecked by remember { mutableStateOf(true) }



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        //  Réduis la taille du canvas pour test
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent) // temporaire pour debug visuel
        ) {



            RepeatingRippleCyberCanvas(
                modifier = Modifier.fillMaxSize(),
                intervalMs = 1000,//1000
                speed = 0.99f,
                amplitude = 150f,//150
                gridSize = 30, //20
                isActive = isChecked,
                colorBase = Color(0xFFF50057),
                colorAccent = Color(0xFF1A181A), // couleur de la vague
                radiusDots = 3.5f,
                frameRate = 32L
            )


            ButtonRockerSwitchVertical(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 52.dp),
                isChecked = isChecked,
                onToggle = { isChecked = it },
                width = 55.dp,
                height = 110.dp,
                cornerRadius = 15.dp,
                animationDuration = 200,
                iconSource = painterResource(R.drawable.emit)
            )







        }


    }
}