package com.lebaillyapp.sonicjammer.composable.knob

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
fun RealisticRotaryKnobBlacked(
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    steps: Int = 30,
    onValueChanged: (Int) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    val sizePx = with(LocalDensity.current) { size.toPx() }

    var rotationAngle by remember { mutableStateOf(0f) }
    var currentStep by remember { mutableStateOf(0) }
    var lastTouchAngle by remember { mutableStateOf<Float?>(null) }

    Box(
        modifier = modifier
            .size(size)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val center = Offset(sizePx / 2, sizePx / 2)
                        lastTouchAngle = angleBetween(center, offset)
                    },
                    onDrag = { change, _ ->
                        val center = Offset(sizePx / 2, sizePx / 2)
                        val newAngle = angleBetween(center, change.position)
                        lastTouchAngle?.let { last ->
                            var delta = newAngle - last
                            if (delta > 180) delta -= 360
                            if (delta < -180) delta += 360

                            rotationAngle = (rotationAngle + delta) % 360f
                            if (rotationAngle < 0) rotationAngle += 360f

                            val newStep = ((rotationAngle / 360f) * steps).roundToInt() % steps
                            if (newStep != currentStep) {
                                currentStep = newStep
                                onValueChanged(currentStep)
                                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                            lastTouchAngle = newAngle
                        }
                    },
                    onDragEnd = { lastTouchAngle = null }
                )
            }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(sizePx / 2f, sizePx / 2f)
            val outerRadius = sizePx / 2f
            val tickOuterRadius = outerRadius
            val bevelRadius = outerRadius * 0.8f
            val knobRadius = bevelRadius * 0.65f
            val innerRadius = knobRadius * 1.20f
            val indicatorRadius = 5.dp.toPx()
            val tickLength = 4.dp.toPx()
            val tickWidth = 2.dp.toPx()
            val tickMargin = 12.dp.toPx()

            // Graduation avec couleur active
            val tickCount = steps
            repeat(tickCount) { i ->
                val angle = Math.toRadians(i * 360f / tickCount.toDouble())
                val start = Offset(
                    x = center.x + (bevelRadius + tickMargin) * cos(angle).toFloat(),
                    y = center.y + (bevelRadius + tickMargin) * sin(angle).toFloat()
                )
                val end = Offset(
                    x = center.x + (bevelRadius + tickMargin + tickLength) * cos(angle).toFloat(),
                    y = center.y + (bevelRadius + tickMargin + tickLength) * sin(angle).toFloat()
                )

                // Couleur orange pour le tick actuel, gris pour les autres
                val tickColor = if (i == currentStep) Color(0xFFFF6B35) else Color(0xFF888888)
                val strokeWidth = if (i == currentStep) tickWidth * 1.5f else tickWidth

                drawLine(
                    color = tickColor,
                    start = start,
                    end = end,
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }

            // Bevel (effet de lumière tournant)
            val angleRad = Math.toRadians(rotationAngle.toDouble())
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

            // === CŒUR DU KNOB AMÉLIORÉ ===
            // Centre creux
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


            // Reflet principal qui tourne avec le knob
            val reflectionAngle = Math.toRadians(rotationAngle.toDouble() - 45)
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





            // Curseur : petit trait orange stylé dans le bevel - MAINTENANT ALIGNÉ
            val indicatorAngle = Math.toRadians(rotationAngle.toDouble()) // Suppression du -90
            val bevelMidRadius = (bevelRadius + knobRadius) / 2f
            val startOffset = Offset(
                center.x + bevelMidRadius * cos(indicatorAngle).toFloat(),
                center.y + bevelMidRadius * sin(indicatorAngle).toFloat()
            )
            val endOffset = Offset(
                center.x + (bevelMidRadius + 10.dp.toPx()) * cos(indicatorAngle).toFloat(),
                center.y + (bevelMidRadius + 10.dp.toPx()) * sin(indicatorAngle).toFloat()
            )
            drawLine(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFA500), Color(0xAAFF8C00), Color.Transparent),
                    center = startOffset,
                    radius = 8.dp.toPx()
                ),
                start = startOffset,
                end = endOffset,
                strokeWidth = 4.dp.toPx(),
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