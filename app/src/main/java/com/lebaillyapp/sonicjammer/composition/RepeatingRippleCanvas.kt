package com.lebaillyapp.sonicjammer.composition

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlin.math.*

/**
 * Composant personnalisé affichant une grille animée avec un effet de vague répétitif
 * centré sur l'écran, à intervalles réguliers.
 *
 * @param modifier Modificateur Composable.
 * @param intervalMs Intervalle entre chaque cycle d'animation en millisecondes.
 * @param amplitude Amplitude maximale du déplacement des points (pixels).
 * @param speed Non utilisé ici (placeholder si on veut adapter la vitesse).
 * @param gridSize Taille des cellules de la grille en pixels.
 * @param isActive Active ou non l'animation répétée.
 */
@Composable
fun RepeatingRippleCanvas(
    modifier: Modifier = Modifier,
    intervalMs: Long = 1500,
    amplitude: Float = 25f,
    gridSize: Int = 50,
    speed: Float = 1f,
    isActive: Boolean = true,
    colorBase: Color = Color.DarkGray,
    radiusDots: Float = 4f
) {
    var waveRadius by remember { mutableStateOf(1f) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    var triggerAnimation by remember { mutableStateOf(false) }

    // Animation fluide de l’amplitude (retour à 0 quand isActive == false)
    val animatedAmplitude by animateFloatAsState(
        targetValue = if (isActive || triggerAnimation) amplitude else 0f,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing),
        label = "animatedAmplitude"
    )

    LaunchedEffect(isActive, speed, intervalMs, canvasSize) {
        while (isActive) {
            triggerAnimation = true
            waveRadius = 0f
            val totalFrames = (intervalMs / 16).toInt()
            val maxDimension = max(canvasSize.width, canvasSize.height)
            val baseStep = if (totalFrames > 0) maxDimension / totalFrames else 0f
            val frameStep = baseStep * speed.coerceAtLeast(0.01f)

            repeat(totalFrames) {
                waveRadius += frameStep
                delay(16)
            }
        }
        // On garde l'effet visuel final jusqu’à ce que l'amplitude retombe à 0
        triggerAnimation = false
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        canvasSize = size
        val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)

        drawGridWithWaveEffect(
            center = center,
            radius = waveRadius,
            amplitude = animatedAmplitude,
            gridSize = gridSize.toFloat(),
            colorBase = colorBase,
            radiusDots = radiusDots
        )
    }
}

/**
 * Dessine une grille avec un effet de vague autour d’un point central.
 *
 * @param center Centre de la vague.
 * @param radius Rayon courant de la vague.
 * @param amplitude Amplitude du déplacement.
 * @param gridSize Taille de la grille.
 */
fun DrawScope.drawGridWithWaveEffect(
    center: Offset,
    radius: Float,
    amplitude: Float,
    gridSize: Float,
    colorBase: Color = Color.DarkGray,
    radiusDots: Float = 4f
) {
    val cols = (size.width / gridSize).toInt()
    val rows = (size.height / gridSize).toInt()

    for (i in 0..cols) {
        for (j in 0..rows) {
            val x = i * gridSize
            val y = j * gridSize
            val distance = Offset(x, y).getODistance(center)

            val waveEffect = if (radius > 1f) {
                sin((distance / radius) * PI).toFloat()
            } else {
                0f // ⬅️ Aucun décalage visuel si l’onde est absente
            }

            val offsetX = waveEffect * amplitude
            val offsetY = waveEffect * amplitude

            drawCircle(
                color = colorBase,
                radius = radiusDots,
                center = Offset(x + offsetX, y + offsetY)
            )
        }
    }
}

/**
 * Calcule la distance entre deux points 2D.
 */
fun Offset.getODistance(other: Offset): Float {
    return sqrt((x - other.x).pow(2) + (y - other.y).pow(2))
}