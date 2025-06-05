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
fun RRKnobV2(
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    steps: Int = 30,
    onValueChanged: (Int) -> Unit,

    // Configuration des repères de graduation
    tickColor: Color = Color(0xFF888888),
    activeTickColor: Color = Color(0xFFFF6B35),
    tickLength: Dp = 4.dp,
    tickWidth: Dp = 2.dp,
    tickSpacing: Dp = 12.dp, // Espacement entre le knob et les repères

    // Configuration du curseur sur le bevel
    indicatorColor: Color = Color(0xFFFFA500),
    indicatorSecondaryColor: Color = Color(0xAAFF8C00),
    indicatorLength: Dp = 10.dp,
    indicatorWidth: Dp = 4.dp,

    // Configuration des tailles
    bevelSizeRatio: Float = 0.8f, // Taille du bevel par rapport au rayon total (0.0 à 1.0)
    knobSizeRatio: Float = 0.65f, // Taille du cœur par rapport au bevel (0.0 à 1.0)

    // Configuration du fond avec effet de profondeur
    showBackground: Boolean = true,
    backgroundColor: Color = Color(0xFF1A1A1A),
    backgroundShadowColor: Color = Color.Black.copy(alpha = 0.6f)
) {
    val initialRotationOffset = -90f
    val haptics = LocalHapticFeedback.current
    val sizePx = with(LocalDensity.current) { size.toPx() }

    var rotationAngle by remember { mutableStateOf(initialRotationOffset) }
    var currentStep by remember { mutableStateOf(0) }
    var lastTouchAngle by remember { mutableStateOf<Float?>(null) }
    var isDragging by remember { mutableStateOf(false) }

    // Animation pour le snap
    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
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
                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
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

                            // Normaliser rotationAngle pour prendre en compte le décalage visuel
                            val normalizedAngle = (rotationAngle - initialRotationOffset + 360f) % 360f

                            val newStep = ((normalizedAngle / 360f) * steps).roundToInt() % steps
                            if (newStep != currentStep) {
                                currentStep = newStep
                                onValueChanged(currentStep)
                                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                            lastTouchAngle = newAngle
                        }
                    },
                    onDragEnd = {
                        isDragging = false
                        lastTouchAngle = null

                        val stepAngle = 360f / steps

                        // Normaliser rotationAngle pour calculer targetStep
                        val normalizedAngle = (rotationAngle - initialRotationOffset + 360f) % 360f

                        val targetStep = ((normalizedAngle / stepAngle) + 0.5f).toInt() % steps

                        // Recalculer l'angle cible en réinjectant le décalage
                        val targetAngle = (targetStep * stepAngle + initialRotationOffset) % 360f

                        val angleDiff = targetAngle - rotationAngle
                        val adjustedTargetAngle = when {
                            angleDiff > 180f -> targetAngle - 360f
                            angleDiff < -180f -> targetAngle + 360f
                            else -> targetAngle
                        }

                        rotationAngle = adjustedTargetAngle

                        if (targetStep != currentStep) {
                            currentStep = targetStep
                            onValueChanged(currentStep)
                        }
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

            // === FOND AVEC EFFET D'ÉLÉVATION INVERSÉE (ENFONCÉ) ===
            if (showBackground) {
                val backgroundRadius = outerRadius + 8.dp.toPx()

                // Fond principal avec dégradé pour simuler la profondeur
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

                // Ombre interne en haut-gauche (simulation de lumière venant du haut-gauche)
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

                // Ombre interne en bas-droite (plus prononcée)
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

                // Bord légèrement surélevé pour accentuer l'effet enfoncé
                drawCircle(
                    color = backgroundColor.copy(alpha = 0.3f),
                    radius = backgroundRadius,
                    center = center,
                    style = Stroke(width = 1.dp.toPx())
                )
            }

            // === GRADUATION AVEC COULEURS CONFIGURABLES ===
            val tickCount = steps
            repeat(tickCount) { i ->
                val angle = Math.toRadians(i * 360f / tickCount.toDouble() + initialRotationOffset)
                val start = Offset(
                    x = center.x + (bevelRadius + tickSpacingPx) * cos(angle).toFloat(),
                    y = center.y + (bevelRadius + tickSpacingPx) * sin(angle).toFloat()
                )
                val end = Offset(
                    x = center.x + (bevelRadius + tickSpacingPx + tickLengthPx) * cos(angle).toFloat(),
                    y = center.y + (bevelRadius + tickSpacingPx + tickLengthPx) * sin(angle).toFloat()
                )

                // Couleur configurable pour le tick actuel vs les autres
                val currentTickColor = if (i == currentStep) activeTickColor else tickColor
                val strokeWidth = if (i == currentStep) tickWidthPx * 1.5f else tickWidthPx

                drawLine(
                    color = currentTickColor,
                    start = start,
                    end = end,
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }

            // === BEVEL (effet de lumière tournant) ===
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

            // === CŒUR DU KNOB CONFIGURABLE ===
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

            // === CURSEUR CONFIGURABLE SUR LE BEVEL ===
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

