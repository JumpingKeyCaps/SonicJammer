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
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlin.math.*

/**
 * Composant stylisé affichant une grille animée avec effet de vagues, pulsations et options visuelles avancées.
 * Permet de créer un rendu visuel "cyber" via l'animation de la couleur, alpha, et grille réticulaire.
 *
 * @param modifier Modificateur Composable.
 * @param intervalMs Intervalle entre chaque cycle d'onde.
 * @param amplitude Amplitude maximale de déplacement.
 * @param gridSize Taille des cellules de la grille.
 * @param speed Vitesse de propagation de l'onde.
 * @param isActive Active ou non les vagues répétées.
 * @param colorBase Couleur de base des points.
 * @param colorAccent Couleur secondaire utilisée dans le gradient animé.
 * @param radiusDots Rayon de chaque point.
 * @param enableAlphaPulse Active ou non les pulsations d'alpha.
 * @param enableDiagonalGrid Active ou non la grille diagonale.
 */
@Composable
fun RepeatingRippleCyberCanvas(
    modifier: Modifier = Modifier,
    intervalMs: Long = 1500,
    amplitude: Float = 25f,
    gridSize: Int = 50,
    speed: Float = 1f,
    isActive: Boolean = true,
    colorBase: Color = Color.DarkGray,
    colorAccent: Color? = null, // <- optionnel, null = couleur fixe
    radiusDots: Float = 4f,
    frameRate: Long = 32L  // 16 = 60fps   32 = 30 fps  42 = 24 fps
) {
    var waveRadius by remember { mutableStateOf(1f) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    var triggerAnimation by remember { mutableStateOf(false) }




    val animatedAmplitude by animateFloatAsState(
        targetValue = if (isActive || triggerAnimation) amplitude else 0f,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing),
        label = "animatedAmplitude"
    )

    LaunchedEffect(isActive, speed, intervalMs, canvasSize) {
        while (isActive) {
            triggerAnimation = true
            waveRadius = 0f
            val totalFrames = (intervalMs / frameRate).toInt()
            val maxDimension = max(canvasSize.width, canvasSize.height)
            val baseStep = if (totalFrames > 0) maxDimension / totalFrames else 0f
            val frameStep = baseStep * speed.coerceAtLeast(0.01f)

            repeat(totalFrames) {
                waveRadius += frameStep
                delay(frameRate)
            }
        }
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
            colorAccent = colorAccent,
            radiusDots = radiusDots
        )
    }
}

/**
 * Dessine une grille avec un effet de vague autour d’un point central.
 * Si colorAccent est non nul, applique un dégradé entre colorBase et colorAccent.
 *
 * @param center Centre de la vague.
 * @param radius Rayon courant de la vague.
 * @param amplitude Amplitude du déplacement.
 * @param gridSize Taille de la grille.
 * @param colorBase Couleur de base des points.
 * @param colorAccent Couleur accentuée (optionnelle) pour dégradé.
 * @param radiusDots Rayon des points.
 */
fun DrawScope.drawGridWithWaveEffect(
    center: Offset,
    radius: Float,
    amplitude: Float,
    gridSize: Float,
    colorBase: Color = Color.DarkGray,
    colorAccent: Color? = null,
    radiusDots: Float = 4f
) {
    val cols = (size.width / gridSize).toInt()
    val rows = (size.height / gridSize).toInt()

    val isWaveInactive = amplitude == 0f || radius <= 1f

    for (i in 0..cols) {
        for (j in 0..rows) {
            val x = i * gridSize
            val y = j * gridSize
            val point = Offset(x, y)
            val vector = point - center
            val distance = vector.getLength()

            val waveEffect = if (!isWaveInactive) {
                sin((distance / radius) * PI).toFloat()
            } else {
                0f
            }

            val unitVector = if (distance > 0f) vector / distance else Offset.Zero
            val offset = unitVector * (waveEffect * amplitude)

            val color = if (!isWaveInactive && colorAccent != null) {
                val t = ((waveEffect + 1f) / 2f).coerceIn(0f, 1f)
                lerp(colorBase, colorAccent, t)
            } else {
                colorAccent?:colorBase
            }

            drawCircle(
                color = color,
                radius = radiusDots,
                center = point + offset
            )
        }
    }
}
/** Calcule la longueur (norme) du vecteur 2D */
fun Offset.getLength(): Float {
    return sqrt(x * x + y * y)
}


