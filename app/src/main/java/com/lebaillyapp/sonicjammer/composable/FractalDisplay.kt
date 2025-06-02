package com.lebaillyapp.sonicjammer.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun FractalDisplay(
    modifier: Modifier = Modifier,
    config: SimpleOscilloscopeConfig = SimpleOscilloscopeConfig()
) {
    val infiniteTransition = rememberInfiniteTransition(label = "fractalAnimation")
    val timeFractal by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000, easing = LinearEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "timeFractal"
    )

    Canvas(modifier = modifier) {
        drawFractal(timeFractal, config)
    }
}

private fun DrawScope.drawFractal(time: Float, config: SimpleOscilloscopeConfig) {
    val centerX = size.width * 0.85f
    val centerY = size.height * 0.15f
    val maxRadius = 40.dp.toPx()

    for (i in 0..80) {
        val t = i / 80f
        val angle = t * 8 * PI + time * 1.5f
        val radius = maxRadius * t * sin(t * 12 + time * 2f)
        val x = centerX + radius * cos(angle).toFloat()
        val y = centerY + radius * sin(angle).toFloat()
        val alpha = (1 - t) * 1f
        val size = (3 - t * 2).dp.toPx()

        drawCircle(config.neonColor.copy(alpha = alpha * 0.3f), size * 2, Offset(x, y))
        drawCircle(config.neonColor.copy(alpha = alpha), size, Offset(x, y))
    }
}