package com.lebaillyapp.sonicjammer.oldStuff.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lebaillyapp.sonicjammer.oldStuff.composable.visualizer.WaveformVisualizer

@Composable
fun DemoFive() {
    var currentFrequency by remember { mutableStateOf(4700f) }
    var factorVisualizer by remember { mutableStateOf(10f) }

    var visualWindowDurationMs by remember { mutableStateOf(3f) }
    var visualResolutionPointsPerMs by remember { mutableStateOf(25f) }
    var amplitude by remember { mutableStateOf(0.9f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${"%,.1f".format(currentFrequency)} Hz",
            style = MaterialTheme.typography.titleMedium,
            color = Color.DarkGray
        )

        Spacer(Modifier.height(12.dp))

        WaveformVisualizer(
            frequencyHz = currentFrequency,
            factorVisualizer = factorVisualizer,
            modifier = Modifier
                .width(350.dp)
                .height(250.dp),
            amplitude = amplitude,
            waveformColor = Color(0xFF1DE9B6),
            visualWindowDurationMs = visualWindowDurationMs,
            visualResolutionPointsPerMs = visualResolutionPointsPerMs
        )

        Spacer(Modifier.height(54.dp))

        // ---- SLIDER : Fréquence ----
        Text(
            text = "Fréquence (1 Hz → 20 000 Hz)",
            color = Color.DarkGray,
            style = MaterialTheme.typography.bodySmall
        )
        Slider(
            value = currentFrequency,
            onValueChange = { currentFrequency = it },
            valueRange = 1f..20000f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // ---- SLIDER : Facteur de visualisation ----
        Spacer(Modifier.height(12.dp))
        Text(
            text = "visual divider factor  : ÷ ${factorVisualizer.toInt()}",
            color = Color.DarkGray,
            style = MaterialTheme.typography.bodySmall
        )
        Slider(
            value = factorVisualizer,
            onValueChange = { factorVisualizer = it.coerceAtLeast(1f) },
            valueRange = 1f..100f,
            steps = 50,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // ---- SLIDER : Durée de fenêtre ----
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Fenêtre temporelle : ${"%.1f".format(visualWindowDurationMs)} ms",
            color = Color.DarkGray,
            style = MaterialTheme.typography.bodySmall
        )
        Slider(
            value = visualWindowDurationMs,
            onValueChange = { visualWindowDurationMs = it },
            valueRange = 1f..200f,
            steps = 200,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // ---- SLIDER : Résolution (points/ms) ----
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Résolution : ${"%.0f".format(visualResolutionPointsPerMs)} points/ms",
            color = Color.DarkGray,
            style = MaterialTheme.typography.bodySmall
        )
        Slider(
            value = visualResolutionPointsPerMs,
            onValueChange = { visualResolutionPointsPerMs = it },
            valueRange = 5f..100f,
            steps = 100,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // ---- SLIDER : Amplitude ----
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Amplitude : ${"%.2f".format(amplitude)}",
            color = Color.DarkGray,
            style = MaterialTheme.typography.bodySmall
        )
        Slider(
            value = amplitude,
            onValueChange = { amplitude = it },
            valueRange = 0.1f..1f,
            steps = 9,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}