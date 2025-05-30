package com.lebaillyapp.sonicjammer.composable.visualizer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.sin
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos


@Composable
fun WaveformVisualizer(
    frequencyHz: Float,
    factorVisualizer: Float = 1f,
    modifier: Modifier = Modifier,
    visualWindowDurationMs: Float = 50f,
    visualResolutionPointsPerMs: Float = 50f,
    amplitude: Float = 0.8f,
    waveformColor: Color = Color.Cyan
) {
    val visualWindowDurationSeconds = visualWindowDurationMs / 1000f
    val totalDisplayPoints = (visualWindowDurationMs * visualResolutionPointsPerMs).toInt().coerceAtLeast(2)

    // Cet état déclenche le recomposé à chaque frame
    var currentTimeSec by remember { mutableStateOf(0f) }

    // Animation "cadencée" à 60 FPS environ, déclenche un recomposé
    LaunchedEffect(Unit) {
        val startTimeNs = System.nanoTime()
        while (true) {
            withFrameNanos { now ->
                val elapsedSec = (now - startTimeNs) / 1_000_000_000f
                currentTimeSec = elapsedSec
            }
        }
    }

    // Calcul dynamique sans remember (car redessiné chaque frame)
    val amplitudes = List(totalDisplayPoints) { i ->
        val time = i.toFloat() / (totalDisplayPoints - 1) * visualWindowDurationSeconds
        val angle = 2 * Math.PI * (frequencyHz/factorVisualizer) * (time + currentTimeSec)
        (amplitude * sin(angle)).toFloat()
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val halfHeight = height / 2f

        val path = Path().apply {
            moveTo(0f, halfHeight + amplitudes[0] * halfHeight)
            for (i in 1 until amplitudes.size) {
                val x = i * (width / (amplitudes.size - 1).toFloat())
                val y = halfHeight + amplitudes[i] * halfHeight
                lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = waveformColor,
            style = Stroke(width = 2f)
        )
    }
}