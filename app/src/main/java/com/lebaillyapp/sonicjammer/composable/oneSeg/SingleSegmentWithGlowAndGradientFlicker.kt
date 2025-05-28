package com.lebaillyapp.sonicjammer.composable.oneSeg

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SingleSegmentWithGlowAndGradientFlicker(
    modifier: Modifier = Modifier,
    segmentLengthDp: Dp = 100.dp,
    segmentWidthDp: Dp = 20.dp,
    bevelSizeDp: Dp = 6.dp,
    glowRadiusDp: Dp = 20.dp,
    segmentBaseColor: Color = Color.Red,
    glowColor: Color = Color.Red,
    baseGlowAlpha: Float = 0.7f,
    flickerAmplitude: Float = 0.1f,
    flickerFrequencyHz: Float = 6f,
    flickerRandom: Boolean = false,
    isOn: Boolean = true
) {
    val density = LocalDensity.current

    val segmentLengthPx = with(density) { segmentLengthDp.toPx() }
    val segmentWidthPx = with(density) { segmentWidthDp.toPx() }
    val bevelSizePx = with(density) { bevelSizeDp.toPx() }
    val glowRadiusPx = with(density) { glowRadiusDp.toPx() }

    val flickerAnim = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(isOn, flickerRandom) {
        if (isOn) {
            if (flickerRandom) {
                while (true) {
                    val randomValue = (Math.random().toFloat() * 2f - 1f)
                    flickerAnim.animateTo(
                        targetValue = randomValue,
                        animationSpec = tween(durationMillis = 50, easing = LinearEasing)
                    )
                }
            } else {
                while (true) {
                    flickerAnim.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = (1000 / flickerFrequencyHz / 2).toInt(), easing = LinearEasing)
                    )
                    flickerAnim.animateTo(
                        targetValue = -1f,
                        animationSpec = tween(durationMillis = (1000 / flickerFrequencyHz / 2).toInt(), easing = LinearEasing)
                    )
                }
            }
        } else {
            flickerAnim.snapTo(0f)
        }
    }

    val flickeredGlowAlpha = if (isOn) {
        (baseGlowAlpha + flickerAnim.value * flickerAmplitude).coerceIn(0f, 1f)
    } else {
        0f // Pas de glow quand OFF
    }

    val canvasWidth = segmentLengthPx + glowRadiusPx * 2
    val canvasHeight = segmentWidthPx + glowRadiusPx * 2

    Canvas(modifier = modifier.size(
        width = with(density) { canvasWidth.toDp() },
        height = with(density) { canvasHeight.toDp() }
    )) {

        val path = Path().apply {
            moveTo(glowRadiusPx + bevelSizePx, glowRadiusPx)
            lineTo(glowRadiusPx + segmentLengthPx - bevelSizePx, glowRadiusPx)
            lineTo(glowRadiusPx + segmentLengthPx, glowRadiusPx + bevelSizePx)
            lineTo(glowRadiusPx + segmentLengthPx, glowRadiusPx + segmentWidthPx - bevelSizePx)
            lineTo(glowRadiusPx + segmentLengthPx - bevelSizePx, glowRadiusPx + segmentWidthPx)
            lineTo(glowRadiusPx + bevelSizePx, glowRadiusPx + segmentWidthPx)
            lineTo(glowRadiusPx, glowRadiusPx + segmentWidthPx - bevelSizePx)
            lineTo(glowRadiusPx, glowRadiusPx + bevelSizePx)
            close()
        }

        val androidPath = path.asAndroidPath()
        if (isOn && flickeredGlowAlpha > 0f) {
            val paint = android.graphics.Paint().apply {
                isAntiAlias = true
                style = android.graphics.Paint.Style.FILL
                color = glowColor.copy(alpha = flickeredGlowAlpha).toArgb()
                maskFilter = BlurMaskFilter(glowRadiusPx, BlurMaskFilter.Blur.NORMAL)
            }
            drawContext.canvas.nativeCanvas.drawPath(androidPath, paint)
        }

        // Couleurs pour le gradient selon l'état ON/OFF
        val fillBrush = if (isOn) {
            Brush.linearGradient(
                colors = listOf(
                    segmentBaseColor.copy(alpha = 0.5f),
                    segmentBaseColor.copy(alpha = 1.0f),
                    segmentBaseColor.copy(alpha = 0.5f)
                ),
                start = Offset(glowRadiusPx, glowRadiusPx + segmentWidthPx / 2),
                end = Offset(glowRadiusPx + segmentLengthPx, glowRadiusPx + segmentWidthPx / 2)
            )
        } else {
            // Version OFF : couleur atténuée, plus grisée
            Brush.linearGradient(
                colors = listOf(
                    segmentBaseColor.copy(alpha = 0.10f).grayscale(),
                    segmentBaseColor.copy(alpha = 0.10f).grayscale(),
                    segmentBaseColor.copy(alpha = 0.10f).grayscale()
                ),
                start = Offset(glowRadiusPx, glowRadiusPx + segmentWidthPx / 2),
                end = Offset(glowRadiusPx + segmentLengthPx, glowRadiusPx + segmentWidthPx / 2)
            )
        }

        drawPath(
            path = path,
            brush = fillBrush,
            style = Fill
        )
    }
}

// Extension pour griser une couleur (conserve alpha)
fun Color.grayscale(): Color {
    val gray = red * 0.299f + green * 0.587f + blue * 0.114f
    return Color(gray, gray, gray, alpha)
}