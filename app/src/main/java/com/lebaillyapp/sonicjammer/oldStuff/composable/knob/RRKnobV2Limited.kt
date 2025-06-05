package com.lebaillyapp.sonicjammer.oldStuff.composable.knob

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


@Composable
fun RRKnobV2Limited(
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    steps: Int = 30,
    minValue: Int = 0,
    maxValue: Int = steps - 1,
    initialValue: Int = 0,
    onValueChanged: (Int) -> Unit,

    // Configuration des repères de graduation
    tickColor: Color = Color(0xFF888888),
    activeTickColor: Color = Color(0xFFFF6B35),
    tickLength: Dp = 4.dp,
    tickWidth: Dp = 2.dp,
    tickSpacing: Dp = 12.dp,

    // Configuration du curseur sur le bevel
    indicatorColor: Color = Color(0xFFFFA500),
    indicatorSecondaryColor: Color = Color(0xAAFF8C00),
    indicatorLength: Dp = 10.dp,
    indicatorWidth: Dp = 4.dp,

    // Configuration des tailles
    bevelSizeRatio: Float = 0.8f,
    knobSizeRatio: Float = 0.65f,

    // Configuration du fond avec effet de profondeur
    showBackground: Boolean = true,
    backgroundColor: Color = Color(0xFF1A1A1A),
    backgroundShadowColor: Color = Color.Black.copy(alpha = 0.6f)
) {

    val haptics = LocalHapticFeedback.current
    val sizePx = with(LocalDensity.current) { size.toPx() }

    // Configuration des angles
    val startAngle = -135f // Position minimum
    val endAngle = 135f    // Position maximum
    val totalAngleRange = endAngle - startAngle // 270°
    val anglePerStep = totalAngleRange / (maxValue - minValue).toFloat()

    var currentValue by remember { mutableStateOf(initialValue.coerceIn(minValue, maxValue)) }
    var isDragging by remember { mutableStateOf(false) }
    var dragStartAngle by remember { mutableStateOf(0f) }
    var dragStartValue by remember { mutableStateOf(0) }

    // Calcul de l'angle actuel basé sur la valeur
    val currentAngle = startAngle + (currentValue - minValue) * anglePerStep

    // Animation pour le snap
    val animatedRotation by animateFloatAsState(
        targetValue = currentAngle,
        animationSpec = tween(
            durationMillis = if (isDragging) 0 else 200,
            easing = EaseOutCubic
        ),
        label = "rotation_animation"
    )

    Box(
        modifier = modifier
            .size(size)
            .pointerInput(Unit) {
                fun angleBetween(center: Offset, point: Offset): Float {
                    val dx = point.x - center.x
                    val dy = point.y - center.y
                    val angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                    return (angle + 360) % 360
                }

                // Clamp un angle dans une plage circulaire qui peut traverser 0
                fun clampAngle(angle: Float, start: Float, end: Float): Float {
                    val a = (angle + 360) % 360
                    val s = (start + 360) % 360
                    val e = (end + 360) % 360
                    return if (s < e) {
                        a.coerceIn(s, e)
                    } else {
                        if (a >= s || a <= e) a else if (a < s) s else e
                    }
                }

                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        val center = Offset(sizePx / 2, sizePx / 2)
                        dragStartAngle = angleBetween(center, offset)
                        dragStartValue = currentValue
                    },
                    onDrag = { change, _ ->
                        val center = Offset(sizePx / 2, sizePx / 2)
                        val rawAngle = angleBetween(center, change.position)

                        // Clamp angle dans [startAngle, endAngle]
                        val clampedAngle = clampAngle(rawAngle, startAngle, endAngle)

                        // Calculer la valeur à partir de l’angle clampé
                        val s = (startAngle + 360) % 360
                        val e = (endAngle + 360) % 360
                        val range = if (s < e) e - s else (e + 360) - s

                        val angleForValue = if (clampedAngle < s) clampedAngle + 360 else clampedAngle
                        val valueFloat = (angleForValue - s) / range * (maxValue - minValue) + minValue
                        val newValue = valueFloat.roundToInt().coerceIn(minValue, maxValue)

                        if (newValue != currentValue) {
                            currentValue = newValue
                            onValueChanged(currentValue)
                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        }
                    },
                    onDragEnd = {
                        isDragging = false
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(sizePx / 2f, sizePx / 2f)
            val outerRadius = sizePx / 2f
            val bevelRadius = outerRadius * bevelSizeRatio
            val knobRadius = bevelRadius * knobSizeRatio
            val innerRadius = knobRadius * 1.20f

            // Conversion des Dp en pixels
            val tickLengthPx = tickLength.toPx()
            val tickWidthPx = tickWidth.toPx()
            val tickSpacingPx = tickSpacing.toPx()
            val indicatorLengthPx = indicatorLength.toPx()
            val indicatorWidthPx = indicatorWidth.toPx()

            // === FOND AVEC EFFET D'ÉLÉVATION INVERSÉE ===
            if (showBackground) {
                val backgroundRadius = outerRadius + 8.dp.toPx()

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            backgroundColor.copy(alpha = 0.8f),
                            backgroundColor,
                            backgroundColor
                        ),
                        center = center,
                        radius = backgroundRadius
                    ),
                    radius = backgroundRadius,
                    center = center
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            backgroundShadowColor.copy(alpha = 0.4f)
                        ),
                        center = Offset(center.x - backgroundRadius * 0.3f, center.y - backgroundRadius * 0.3f),
                        radius = backgroundRadius * 0.8f
                    ),
                    radius = backgroundRadius,
                    center = center
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            backgroundShadowColor.copy(alpha = 0.6f),
                            backgroundShadowColor.copy(alpha = 0.3f),
                            Color.Transparent
                        ),
                        center = Offset(center.x + backgroundRadius * 0.2f, center.y + backgroundRadius * 0.2f),
                        radius = backgroundRadius * 0.9f
                    ),
                    radius = backgroundRadius,
                    center = center
                )

                drawCircle(
                    color = backgroundColor.copy(alpha = 0.3f),
                    radius = backgroundRadius,
                    center = center,
                    style = Stroke(width = 1.dp.toPx())
                )
            }

            // === GRADUATION LIMITÉE ===
            for (i in minValue..maxValue) {
                val tickAngle = startAngle + (i - minValue) * anglePerStep
                val angle = Math.toRadians(tickAngle.toDouble())

                val start = Offset(
                    x = center.x + (bevelRadius + tickSpacingPx) * cos(angle).toFloat(),
                    y = center.y + (bevelRadius + tickSpacingPx) * sin(angle).toFloat()
                )
                val end = Offset(
                    x = center.x + (bevelRadius + tickSpacingPx + tickLengthPx) * cos(angle).toFloat(),
                    y = center.y + (bevelRadius + tickSpacingPx + tickLengthPx) * sin(angle).toFloat()
                )

                val currentTickColor = if (i == currentValue) activeTickColor else tickColor
                val strokeWidth = if (i == currentValue) tickWidthPx * 1.5f else tickWidthPx

                drawLine(
                    color = currentTickColor,
                    start = start,
                    end = end,
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }

            // === BEVEL ===
            val angleRad = Math.toRadians(animatedRotation.toDouble())
            val lightDirection = Offset(
                x = center.x + bevelRadius * cos(angleRad).toFloat(),
                y = center.y + bevelRadius * sin(angleRad).toFloat()
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF252323), Color(0xFF111111)),
                    center = lightDirection,
                    radius = bevelRadius * 1.2f
                ),
                radius = bevelRadius,
                center = center
            )

            // === CŒUR DU KNOB ===
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF070707),
                        Color(0xFF100F0F),
                        Color(0xFF151414)
                    ),
                    center = center,
                    radius = innerRadius
                ),
                radius = innerRadius,
                center = center
            )

            // Reflet principal
            val reflectionAngle = Math.toRadians(animatedRotation.toDouble() - 45)
            val reflectionCenter = Offset(
                center.x - knobRadius * 0.4f * cos(reflectionAngle).toFloat(),
                center.y - knobRadius * 0.4f * sin(reflectionAngle).toFloat()
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.05f),
                        Color.White.copy(alpha = 0.025f),
                        Color.White.copy(alpha = 0.005f),
                        Color.Transparent
                    ),
                    center = reflectionCenter,
                    radius = knobRadius * 0.9f
                ),
                radius = knobRadius,
                center = center
            )

            // === CURSEUR SUR LE BEVEL ===
            val indicatorAngle = Math.toRadians(animatedRotation.toDouble())
            val bevelMidRadius = (bevelRadius + knobRadius) / 2f
            val startOffset = Offset(
                center.x + bevelMidRadius * cos(indicatorAngle).toFloat(),
                center.y + bevelMidRadius * sin(indicatorAngle).toFloat()
            )
            val endOffset = Offset(
                center.x + (bevelMidRadius + indicatorLengthPx) * cos(indicatorAngle).toFloat(),
                center.y + (bevelMidRadius + indicatorLengthPx) * sin(indicatorAngle).toFloat()
            )

            drawLine(
                brush = Brush.radialGradient(
                    colors = listOf(indicatorColor, indicatorSecondaryColor, Color.Transparent),
                    center = startOffset,
                    radius = indicatorWidthPx * 2f
                ),
                start = startOffset,
                end = endOffset,
                strokeWidth = indicatorWidthPx,
                cap = StrokeCap.Round
            )
        }
    }
}

private fun angleBetween(center: Offset, touch: Offset): Float {
    val dx = touch.x - center.x
    val dy = touch.y - center.y
    val theta = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
    return (theta + 360f) % 360f
}

fun clampAngle(angle: Float, start: Float, end: Float): Float {
    val normAngle = (angle + 360) % 360
    val normStart = (start + 360) % 360
    val normEnd = (end + 360) % 360

    return if (normEnd < normStart) {
        // Intervalle qui traverse 0°
        val extendedEnd = normEnd + 360
        val extendedAngle = if (normAngle < normStart) normAngle + 360 else normAngle
        extendedAngle.coerceIn(normStart, extendedEnd) % 360
    } else {
        // Intervalle classique
        normAngle.coerceIn(normStart, normEnd)
    }
}