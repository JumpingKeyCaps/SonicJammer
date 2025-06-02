package com.lebaillyapp.sonicjammer.composable

import androidx.compose.ui.graphics.drawscope.Stroke


import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.*

/**
 * Configuration simplifiée de l'oscilloscope
 */
data class SimpleOscilloscopeConfig(
    val neonColor: Color = Color(0xFF00FF41), // Vert néon
    val backgroundColor: Color = Color(0xFF0A0A0A),
    val gridColor: Color = Color(0xFF1A4A1A),
    val glowIntensity: Float = 0.85f,
    val scanlineIntensity: Float = 0.3f
)

/**
 * Oscilloscope Néon Simplifié - Lissajous et Fractale uniquement
 */
enum class LissajousStyle {
    CLASSIC,
    ELLIPSE,
    FIGURE_8,
    SPIRO,
    CUSTOM,
    SQUARE,
    TRIANGLE,
    FLOWER,
    INFINITY,
    BOWTIE,
    BUTTERFLY,
    HEART,
    STAR,
    VORTEX,
    ROSE,
    ORBIT,
    WOBBLE,
    SPIRAL,
    WAVES,
    DNA,
    CHAOS,
    // --- nouvelles formes ---
    LISSAJOUS_3_4,
    LISSAJOUS_5_6,
    LISSAJOUS_7_9,
    LISSAJOUS_2_5_DELTA0,
    LISSAJOUS_2_5_DELTA90,
    LISSAJOUS_4_3_DELTA45,
    LISSAJOUS_6_5_DELTA30
}

@Composable
fun SimpleNeonOscilloscope(
    modifier: Modifier = Modifier,
    frequency: Float = 17800f,
    config: SimpleOscilloscopeConfig = SimpleOscilloscopeConfig(),
    isActive: Boolean = true,
    signalStrength: Float = 1.0f,
    lissajousZoom: Float = 1.0f,
    figureStyle: LissajousStyle = LissajousStyle.CLASSIC,
    color1: Color = config.neonColor,
    color2: Color = config.neonColor.copy(alpha = 0.5f),

    // Nouveaux paramètres custom pour LissajousStyle.CUSTOM
    customFov: Float = 300f,
    customRotationSpeed: Float = 2f,
    customA: Float? = null,
    customB: Float? = null,
    customDelta: Float? = null,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "oscilloscope")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600000, easing = LinearEasing)
        ),
        label = "time"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(config.backgroundColor)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (isActive) {
                drawGrid(config)
                drawLissajous(
                    frequency, time, config, signalStrength, lissajousZoom,
                    figureStyle, color1, color2,
                    customFov, customRotationSpeed,
                    customA, customB, customDelta
                )
            } else {
                drawInactiveScreen(config)
            }
        }


        Canvas(modifier = Modifier.fillMaxSize()) {
            drawScanlines(config)
        }
    }
}


private fun DrawScope.drawLissajous(
    frequency: Float,
    time: Float,
    config: SimpleOscilloscopeConfig,
    signalStrength: Float,
    zoom: Float,
    figureStyle: LissajousStyle,
    color1: Color,
    color2: Color,
    customFov: Float = 300f,
    customRotationSpeed: Float = 1f,
    customA: Float? = null,
    customB: Float? = null,
    customDelta: Float? = null,
) {
    val centerX = size.width / 2f
    val centerY = size.height / 2f

    val baseRadius = minOf(size.width, size.height) * 0.25f
    val zoomClamped = zoom.coerceIn(0.1f, 5.0f)
    val radius = baseRadius * zoomClamped
    val maxAllowedRadius = minOf(size.width, size.height) * 0.45f
    val finalRadius = minOf(radius, maxAllowedRadius)

    val normalizedFreq = frequency / 1000f
    val intensity = signalStrength * config.glowIntensity
    val pointCount = (200 + (zoomClamped * 50).toInt()).coerceAtMost(400)

    var a = normalizedFreq
    var b = normalizedFreq * 1.618f
    var delta = time

    when (figureStyle) {
        LissajousStyle.CLASSIC -> {}
        LissajousStyle.ELLIPSE -> { a = normalizedFreq; b = normalizedFreq; delta = 0f }
        LissajousStyle.FIGURE_8 -> { a = normalizedFreq; b = normalizedFreq; delta = PI.toFloat() / 2 }
        LissajousStyle.SPIRO -> { a = normalizedFreq * 3f; b = normalizedFreq * 2f; delta = time * 0.5f }
        LissajousStyle.CUSTOM -> {
            // ici on priorise les valeurs custom s'ils sont définis
            a = customA ?: normalizedFreq
            b = customB ?: (normalizedFreq * 1.3f)
            delta = customDelta ?: (time * 0.7f)
        }
        LissajousStyle.SQUARE -> { a = 1f; b = 1f; delta = PI.toFloat() / 2 }
        LissajousStyle.TRIANGLE -> { a = 2f; b = 3f; delta = PI.toFloat() / 4 }
        LissajousStyle.FLOWER -> { a = 3f; b = 5f; delta = time * 0.5f }
        LissajousStyle.INFINITY -> { a = 2f; b = 2f; delta = PI.toFloat() / 2 }
        LissajousStyle.BOWTIE -> { a = 4f; b = 1f; delta = PI.toFloat() / 2 }
        LissajousStyle.BUTTERFLY -> { a = 5f; b = 4f; delta = time * 0.6f }
        LissajousStyle.HEART -> { a = 6f; b = 5f; delta = PI.toFloat() / 2 }
        LissajousStyle.STAR -> { a = 5f; b = 2f; delta = time * 0.4f }
        LissajousStyle.VORTEX -> { a = 3f; b = 3f; delta = time * 0.8f }
        LissajousStyle.ROSE -> { a = 6f; b = 1f; delta = time * 0.3f }
        LissajousStyle.ORBIT -> { a = 2f; b = 1f; delta = PI.toFloat() / 3 }
        LissajousStyle.WOBBLE -> { a = 2f; b = 2.5f; delta = time * 0.6f }
        LissajousStyle.SPIRAL -> { a = 1f + time % 3f; b = 1.5f + (time % 2f); delta = time * 0.9f }
        LissajousStyle.WAVES -> { a = 0.5f; b = 3f; delta = time * 0.4f }
        LissajousStyle.DNA -> { a = 1f; b = 2f; delta = time * 1.2f }
        LissajousStyle.CHAOS -> { a = Math.random().toFloat() * 10f; b = Math.random().toFloat() * 10f; delta = time * 0.7f }
        LissajousStyle.LISSAJOUS_3_4 -> { a = 3f; b = 4f; delta = 0f }
        LissajousStyle.LISSAJOUS_5_6 -> { a = 5f; b = 6f; delta = 0f }
        LissajousStyle.LISSAJOUS_7_9 -> { a = 7f; b = 9f; delta = 0f }
        LissajousStyle.LISSAJOUS_2_5_DELTA0 -> { a = 2f; b = 5f; delta = 0f }
        LissajousStyle.LISSAJOUS_2_5_DELTA90 -> { a = 2f; b = 5f; delta = PI.toFloat() / 2 }
        LissajousStyle.LISSAJOUS_4_3_DELTA45 -> { a = 4f; b = 3f; delta = PI.toFloat() / 4 }
        LissajousStyle.LISSAJOUS_6_5_DELTA30 -> { a = 6f; b = 5f; delta = PI.toFloat() / 6 }
    }


    val rotationSpeed = PI.toFloat() / customRotationSpeed
    val angleY = time * rotationSpeed
    val cosY = cos(angleY)
    val sinY = sin(angleY)

    val path1 = Path()
    val path2 = Path()
    var useColor1 = true
    var isFirst = true

    for (i in 0..pointCount) {
        val t = i / pointCount.toFloat() * 4 * PI

        // Coordonnées 3D "virtuelles"
        val rawX = sin(a * t + delta).toFloat() * finalRadius
        val rawY = sin(b * t).toFloat() * finalRadius
        val rawZ = cos(a * t).toFloat() * finalRadius // simulate Z with another function

        // Rotation 3D autour de Y
        val rotatedX = rawX * cosY - rawZ * sinY
        val rotatedZ = rawX * sinY + rawZ * cosY

        // Projection perspective 3D -> 2D
        val scale = customFov / (customFov + rotatedZ)
        val x = centerX + rotatedX * scale
        val y = centerY + rawY * scale

        if (useColor1) {
            if (isFirst) {
                path1.moveTo(x, y)
                isFirst = false
            } else {
                path1.lineTo(x, y)
            }
        } else {
            if (path2.isEmpty) {
                path2.moveTo(x, y)
            } else {
                path2.lineTo(x, y)
            }
        }
        useColor1 = !useColor1
    }

    val glowLayers = if (zoomClamped > 2.0f) 4 else 3
    for (glowLayer in 1..glowLayers) {
        val glowWidth = (glowLayer * 2 * (0.5f + zoomClamped * 0.3f)).dp.toPx()
        val glowAlpha = (intensity / glowLayer) * (0.6f * (1f - zoomClamped * 0.1f)).coerceAtLeast(0.2f)

        drawPath(
            path = path1,
            color = color1.copy(alpha = glowAlpha),
            style = Stroke(
                width = glowWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        drawPath(
            path = path2,
            color = color2.copy(alpha = glowAlpha),
            style = Stroke(
                width = glowWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }

    val mainStrokeWidth = (2f + zoomClamped * 0.5f).dp.toPx()

    drawPath(
        path = path1,
        color = color1.copy(alpha = intensity),
        style = Stroke(
            width = mainStrokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

    drawPath(
        path = path2,
        color = color2.copy(alpha = intensity),
        style = Stroke(
            width = mainStrokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

private fun DrawScope.drawGrid(config: SimpleOscilloscopeConfig) {
    val gridSpacing = 40.dp.toPx()
    val strokeWidth = 1.dp.toPx()
    var x = 0f
    while (x <= size.width) {
        drawLine(config.gridColor, Offset(x, 0f), Offset(x, size.height), strokeWidth, alpha = 0.3f)
        x += gridSpacing
    }
    var y = 0f
    while (y <= size.height) {
        drawLine(config.gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth, alpha = 0.3f)
        y += gridSpacing
    }
    drawLine(config.gridColor, Offset(size.width / 2, 0f), Offset(size.width / 2, size.height), strokeWidth * 2, alpha = 0.5f)
    drawLine(config.gridColor, Offset(0f, size.height / 2), Offset(size.width, size.height / 2), strokeWidth * 2, alpha = 0.5f)
}



private fun DrawScope.drawScanlines(config: SimpleOscilloscopeConfig) {
    val lineSpacing = 4.dp.toPx()
    var y = 0f
    while (y <= size.height) {
        drawLine(Color.White.copy(alpha = config.scanlineIntensity * 0.08f), Offset(0f, y), Offset(size.width, y), 1.dp.toPx())
        y += lineSpacing
    }
}

private fun DrawScope.drawInactiveScreen(config: SimpleOscilloscopeConfig) {
    drawCircle(
        color = config.gridColor.copy(alpha = 0.3f),
        radius = 2.dp.toPx(),
        center = Offset(size.width / 2f, size.height / 2f)
    )
}
