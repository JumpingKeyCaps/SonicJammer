package com.lebaillyapp.sonicjammer.composable.sevenSeg

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.sin


@Composable
fun SevenSegmentDisplayExtended(
    modifier: Modifier = Modifier,
    digit: Int? = null,
    char: Char? = null,
    manualSegments: List<Boolean>? = null,
    segmentLength: Dp = 80.dp,
    segmentHorizontalLength: Dp = 80.dp,
    segmentThickness: Dp = 20.dp,
    bevel: Dp = 6.dp,
    onColor: Color = Color.Red,
    offColor: Color = Color.DarkGray,
    alpha: Float = 1f,
    glowRadius: Float = 15f,
    flickerAmplitude: Float = 0.1f,
    flickerFrequency: Float = 3f,
    idleMode: Boolean = false,
    idleSpeed: Long = 100
) {
    val density = LocalDensity.current
    val segLenPx = with(density) { segmentLength.toPx() }
    val segHorLenPx = with(density) { segmentHorizontalLength.toPx() }
    val segThkPx = with(density) { segmentThickness.toPx() }
    val bevelPx = with(density) { bevel.toPx() }

    var segments by remember { mutableStateOf(manualSegments) }

    val infiniteTransition = rememberInfiniteTransition()
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Restart)
    )

    val flickerAlpha = remember {
        List(7) { kotlin.random.Random(it).nextFloat() * 0.5f + 0.5f }
    }

    fun flicker(index: Int): Double {
        val base = flickerAlpha.getOrNull(index) ?: 1f
        return alpha * (base + flickerAmplitude * kotlin.math.sin(2 * Math.PI * flickerFrequency * time + index))
            .coerceIn(0.0, 1.0)
    }

    fun segmentHorizontal(x: Float, y: Float): Path = Path().apply {
        moveTo(x + bevelPx, y)
        lineTo(x + segHorLenPx - bevelPx, y)
        lineTo(x + segHorLenPx, y + bevelPx)
        lineTo(x + segHorLenPx, y + segThkPx - bevelPx)
        lineTo(x + segHorLenPx - bevelPx, y + segThkPx)
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

    val A = Offset(segThkPx, 0f)
    val B = Offset(segHorLenPx + segThkPx, segThkPx)
    val C = Offset(segHorLenPx + segThkPx, segLenPx + 2 * segThkPx)
    val D = Offset(segThkPx, 2 * segLenPx + 2 * segThkPx)
    val E = Offset(0f, segLenPx + 2 * segThkPx)
    val F = Offset(0f, segThkPx)
    val G = Offset(segThkPx, segLenPx + segThkPx)

    val digitSegmentMap = mapOf(
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

    val charSegmentMap = mapOf(
        'A' to listOf(true, true, true, false, true, true, true),
        'B' to listOf(false, false, true, true, true, true, true),
        'C' to listOf(true, false, false, true, true, true, false),
        'D' to listOf(false, true, true, true, true, false, true),
        'E' to listOf(true, false, false, true, true, true, true),
        'F' to listOf(true, false, false, false, true, true, true),
        'H' to listOf(false, true, true, false, true, true, true),
        'L' to listOf(false, false, false, true, true, true, false),
        'P' to listOf(true, true, false, false, true, true, true),
        'U' to listOf(false, true, true, true, true, true, false)
    )

    val states = segments ?: run {
        when {
            digit != null -> digitSegmentMap[digit]
            char != null -> charSegmentMap[char.uppercaseChar()]
            else -> null
        }
    } ?: List(7) { false }

    val widthPx = segHorLenPx + 2 * segThkPx
    val heightPx = 2 * segLenPx + 3 * segThkPx

    // Idle mode animation states
    val idle1 by remember { mutableStateOf(listOf(true, false, false, true, false, false, false)) }
    val idle2 by remember { mutableStateOf(listOf(false, true, false, false, true, false, false)) }
    val idle3 by remember { mutableStateOf(listOf(false, false, true, false, false, true, false)) }
    var spinspin by remember { mutableIntStateOf(0) }
    val idleSegAnim by remember { mutableStateOf(listOf(idle1, idle2, idle3)) }

    LaunchedEffect(idleMode) {
        if (idleMode) {
            while (true) {
                delay(idleSpeed)
                spinspin = (spinspin + 1) % idleSegAnim.size
                segments = idleSegAnim[spinspin]
            }
        }
    }

    Canvas(modifier = modifier.size(with(density) { widthPx.toDp() }, with(density) { heightPx.toDp() })) {
        val paintGlow = Paint().apply {
            this.color = onColor
            this.alpha = 255f
            this.asFrameworkPaint().maskFilter = BlurMaskFilter(glowRadius, BlurMaskFilter.Blur.NORMAL)
        }

        fun drawSegment(path: Path, isOn: Boolean, index: Int, alphaOverride: Float = alpha) {
            val flick = flicker(index) * alphaOverride
            if (isOn) {
                drawPath(path, onColor.copy(alpha = flick.toFloat()))
                drawContext.canvas.drawPath(path, paintGlow)
            } else {
                drawPath(path, offColor.copy(alpha = alphaOverride * 0.6f))
            }
        }

        // Dessin des segments
        drawSegment(segmentHorizontal(A.x, A.y), states[0], 0)
        drawSegment(segmentVertical(B.x, B.y), states[1], 1)
        drawSegment(segmentVertical(C.x, C.y), states[2], 2)
        drawSegment(segmentHorizontal(D.x, D.y), states[3], 3)
        drawSegment(segmentVertical(E.x, E.y), states[4], 4)
        drawSegment(segmentVertical(F.x, F.y), states[5], 5)
        drawSegment(segmentHorizontal(G.x, G.y), states[6], 6)
    }
}