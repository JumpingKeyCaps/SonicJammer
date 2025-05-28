package com.lebaillyapp.sonicjammer.composable.sevenSeg


import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sin

@Composable
fun SevenSegmentDisplay(
    digit: Int,
    modifier: Modifier = Modifier,
    segmentLength: Dp = 80.dp,
    segmentThickness: Dp = 20.dp,
    bevel: Dp = 6.dp,
    onColor: Color = Color.Red,
    offColor: Color = Color.DarkGray,
    alpha: Float = 1f,
    glowRadius: Float = 15f, // en pixels pour RenderEffect
    flickerAmplitude: Float = 0.1f,
    flickerFrequency: Float = 3f // en Hz
) {
    val density = LocalDensity.current
    val segLenPx = with(density) { segmentLength.toPx() }
    val segThkPx = with(density) { segmentThickness.toPx() }
    val bevelPx = with(density) { bevel.toPx() }

    // Animation time en secondes
    val infiniteTransition = rememberInfiniteTransition()
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Restart
        )
    )

    // Flicker random calculé via sinusoïde + variation aléatoire stable
    // Je mixe sin(time) avec un noise random fixe (seedé)
    val flickerAlpha = remember {
        List(7) { kotlin.random.Random(it).nextFloat() * 0.5f + 0.5f } // base noise [0.5..1]
    }

    fun flicker(index: Int): Double {
        val base = flickerAlpha.getOrNull(index) ?: 1f
        // oscillation sinusoïdale + base bruit
        return alpha * (base + flickerAmplitude * sin(2 * Math.PI * flickerFrequency * time + index))
            .coerceIn(0.0, 1.0)
    }

    // Paths identiques au précédent, pas d’astuce ici pour biseaux nets

    fun segmentHorizontal(x: Float, y: Float): Path = Path().apply {
        moveTo(x + bevelPx, y)
        lineTo(x + segLenPx - bevelPx, y)
        lineTo(x + segLenPx, y + bevelPx)
        lineTo(x + segLenPx, y + segThkPx - bevelPx)
        lineTo(x + segLenPx - bevelPx, y + segThkPx)
        lineTo(x + bevelPx, y + segThkPx)
        lineTo(x, y + segThkPx - bevelPx)
        lineTo(x, y + bevelPx)
        close()
    }

    fun segmentVertical(x: Float, y: Float): Path = Path().apply {
        moveTo(x, y + bevelPx)
        lineTo(x + bevelPx, y)
        lineTo(x + segThkPx - bevelPx, y)
        lineTo(x + segThkPx, y + bevelPx)
        lineTo(x + segThkPx, y + segLenPx - bevelPx)
        lineTo(x + segThkPx - bevelPx, y + segLenPx)
        lineTo(x + bevelPx, y + segLenPx)
        lineTo(x, y + segLenPx - bevelPx)
        close()
    }

    // Positions des segments
    val A = Offset(segThkPx, 0f)
    val B = Offset(segLenPx + segThkPx, segThkPx)
    val C = Offset(segLenPx + segThkPx, segLenPx + 2 * segThkPx)
    val D = Offset(segThkPx, 2 * segLenPx + 2 * segThkPx)
    val E = Offset(0f, segLenPx + 2 * segThkPx)
    val F = Offset(0f, segThkPx)
    val G = Offset(segThkPx, segLenPx + segThkPx)

    val digitMap = mapOf(
        0 to listOf(true, true, true, true, true, true, false),
        1 to listOf(false, true, true, false, false, false, false),
        2 to listOf(true, true, false, true, true, false, true),
        3 to listOf(true, true, true, true, false, false, true),
        4 to listOf(false, true, true, false, false, true, true),
        5 to listOf(true, false, true, true, false, true, true),
        6 to listOf(true, false, true, true, true, true, true),
        7 to listOf(true, true, true, false, false, false, false),
        8 to listOf(true, true, true, true, true, true, true),
        9 to listOf(true, true, true, true, false, true, true)
    )
    val states = digitMap[digit] ?: List(7) { false }

    val widthPx = segLenPx + 2 * segThkPx
    val heightPx = 2 * segLenPx + 3 * segThkPx

    Canvas(modifier = modifier.size(with(density) { widthPx.toDp() }, with(density) { heightPx.toDp() })) {
        // Glow effect RenderEffect : simple blur autour du segment
        val paintGlow = Paint().apply {
            this.color = onColor
            this.alpha = 255F
            this.asFrameworkPaint().setMaskFilter(android.graphics.BlurMaskFilter(glowRadius, android.graphics.BlurMaskFilter.Blur.NORMAL))
        }
        val paintNormal = Paint()

        fun drawSegment(path: Path, isOn: Boolean, index: Int) {
            val flick = flicker(index)
            if (isOn) {
                // Glow + couleur allumée avec flicker alpha
                drawPath(path, onColor.copy(alpha = flick.toFloat()))
                // Overlay glow (dessiner 2 fois en blur ?)
                drawContext.canvas.drawPath(path, paintGlow)
            } else {
                // segment éteint avec alpha global
                drawPath(path, offColor.copy(alpha = alpha * 0.6f))
            }
        }

        drawSegment(segmentHorizontal(A.x, A.y), states[0], 0)
        drawSegment(segmentVertical(B.x, B.y), states[1], 1)
        drawSegment(segmentVertical(C.x, C.y), states[2], 2)
        drawSegment(segmentHorizontal(D.x, D.y), states[3], 3)
        drawSegment(segmentVertical(E.x, E.y), states[4], 4)
        drawSegment(segmentVertical(F.x, F.y), states[5], 5)
        drawSegment(segmentHorizontal(G.x, G.y), states[6], 6)
    }
}

