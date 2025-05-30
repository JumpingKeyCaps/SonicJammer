package com.lebaillyapp.sonicjammer.composable.fourteenSeg

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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.sonicjammer.config.FourteenSegmentConfig
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random





@Composable
fun FourteenSegmentDisplayExtended(
    modifier: Modifier = Modifier,
    config: FourteenSegmentConfig = FourteenSegmentConfig(),
    digit: Int? = null,
    char: Char? = null,
    manualSegments: List<Boolean>? = null,
) {
    val density = LocalDensity.current
    val segLenPx = with(density) { config.segmentLength.toPx() }
    val segHorLenPx = with(density) { config.segmentHorizontalLength.toPx() }
    val segThkPx = with(density) { config.segmentThickness.toPx() }
    val bevelPx = with(density) { config.bevel.toPx() }

    // Initialisation des segments
    var segments = when {
        manualSegments != null -> manualSegments
        digit != null -> digitSegmentMap[digit]
        char != null -> charSegmentMap[char]
        config.manualSegments != null -> config.manualSegments
        config.digit != null -> digitSegmentMap[config.digit]
        config.char != null -> charSegmentMap[config.char]
        else -> List(14) { false }
    } ?: List(14) { false }

    val infiniteTransition = rememberInfiniteTransition()
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Restart)
    )

    val flickerAlpha = remember {
        List(14) { kotlin.random.Random(it).nextFloat() * 0.5f + 0.5f }
    }

    fun flicker(index: Int): Double {
        val base = flickerAlpha.getOrNull(index) ?: 1f
        return config.alpha * (base + config.flickerAmplitude * kotlin.math.sin(2 * PI * config.flickerFrequency * time + index))
            .coerceIn(0.0, 1.0)
    }

    // Function to draw a full horizontal segment
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

    // Function to draw a full vertical segment
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

    // Function to draw a diagonal segment
    fun segmentDiagonal(x1: Float, y1: Float, x2: Float, y2: Float): Path {
        val dx = x2 - x1
        val dy = y2 - y1
        val length = sqrt(dx * dx + dy * dy)
        if (length == 0f) return Path()

        val ux = dx / length
        val uy = dy / length

        val halfThk = segThkPx / 2f

        // Vecteur perpendiculaire
        val px = -uy * halfThk
        val py = ux * halfThk

        // Vecteur dans la direction pour biseau
        val bx = ux * halfThk
        val by = uy * halfThk

        return Path().apply {
            moveTo(x1 - bx + px, y1 - by + py) // coin 1
            lineTo(x2 + bx + px, y2 + by + py) // coin 2
            lineTo(x2 + bx - px, y2 + by - py) // coin 3
            lineTo(x1 - bx - px, y1 - by - py) // coin 4
            close()
        }
    }

    // Function to draw a half horizontal segment (used for G1 and G2)
    fun segmentHorizontalHalf(x: Float, y: Float): Path = Path().apply {
        val halfLength = (segHorLenPx - segThkPx) / 2
        moveTo(x + bevelPx, y)
        lineTo(x + halfLength - bevelPx, y)
        lineTo(x + halfLength, y + bevelPx)
        lineTo(x + halfLength, y + segThkPx - bevelPx)
        lineTo(x + halfLength - bevelPx, y + segThkPx)
        lineTo(x + bevelPx, y + segThkPx)
        lineTo(x, y + segThkPx - bevelPx)
        lineTo(x, y + bevelPx)
        close()
    }

    // Calculate dimensions and positions
    val widthPx = segHorLenPx + 2 * segThkPx
    val heightPx = 2 * segLenPx + 3 * segThkPx
    val centerX = widthPx / 2f
    val midYForDiagonals = segLenPx + segThkPx + segThkPx / 2f

    // Segment positions
    val A = Offset(segThkPx, 0f)
    val B = Offset(segHorLenPx + segThkPx, segThkPx)
    val C = Offset(segHorLenPx + segThkPx, segLenPx + 2 * segThkPx)
    val D = Offset(segThkPx, 2 * segLenPx + 2 * segThkPx)
    val E = Offset(0f, segLenPx + 2 * segThkPx)
    val F = Offset(0f, segThkPx)
    val G1 = Offset(segThkPx, segLenPx + segThkPx)
    val G2 = Offset(segThkPx + (segHorLenPx - segThkPx) / 2 + segThkPx, segLenPx + segThkPx)
    val H1 = Offset(segThkPx + bevelPx, segThkPx + bevelPx) // Top-left inner
    val H2 = Offset(centerX, segLenPx + segThkPx)           // Center
    val I = Offset(centerX - segThkPx/2, segThkPx)          // Top vertical center
    val J1 = Offset(segHorLenPx + segThkPx - bevelPx, segThkPx + bevelPx) // Top-right inner
    val J2 = Offset(centerX, segLenPx + segThkPx)           // Center
    val K1 = Offset(segHorLenPx + segThkPx - bevelPx, 2*segLenPx + segThkPx - bevelPx) // Symétrique de J1
    val K2 = Offset(centerX, segLenPx + segThkPx)           // Center (même point)
    val L = Offset(centerX - segThkPx/2, segLenPx + 2*segThkPx) // Symétrique de I
    val M1 = Offset(segThkPx + bevelPx, 2*segLenPx + segThkPx - bevelPx) // Symétrique de H1
    val M2 = Offset(centerX, segLenPx + segThkPx)           // Center (même point)



    Canvas(modifier = modifier.size(with(density) { widthPx.toDp() }, with(density) { heightPx.toDp() })) {
        val paintGlow = Paint().apply {
            color = config.onColor
            alpha = 255f
            asFrameworkPaint().maskFilter = BlurMaskFilter(config.glowRadius, BlurMaskFilter.Blur.NORMAL)
        }

        fun drawSegment(path: Path, isOn: Boolean, index: Int, alphaOverride: Float = config.alpha) {
            val flick = flicker(index) * alphaOverride
            if (isOn) {
                drawPath(path, config.onColor.copy(alpha = flick.toFloat()))
                drawContext.canvas.drawPath(path, paintGlow)
            } else {
                drawPath(path, config.offColor.copy(alpha = alphaOverride * 0.6f))
            }
        }

        // Draw all 14 segments
        drawSegment(segmentDiagonal(J1.x, J1.y, J2.x, J2.y), segments[10], 10) // J
        drawSegment(segmentDiagonal(K1.x, K1.y, K2.x, K2.y), segments[11], 11) // K
        drawSegment(segmentDiagonal(M1.x, M1.y, M2.x, M2.y), segments[13], 13) // M
        drawSegment(segmentDiagonal(H1.x, H1.y, H2.x, H2.y), segments[8], 8)   // H
        drawSegment(segmentHorizontal(A.x, A.y), segments[0], 0)        // A
        drawSegment(segmentVertical(B.x, B.y), segments[1], 1)          // B
        drawSegment(segmentVertical(C.x, C.y), segments[2], 2)          // C
        drawSegment(segmentHorizontal(D.x, D.y), segments[3], 3)        // D
        drawSegment(segmentVertical(E.x, E.y), segments[4], 4)          // E
        drawSegment(segmentVertical(F.x, F.y), segments[5], 5)          // F
        drawSegment(segmentHorizontalHalf(G1.x, G1.y), segments[6], 6)  // G1
        drawSegment(segmentHorizontalHalf(G2.x, G2.y), segments[7], 7)  // G2
        drawSegment(segmentVertical(L.x, L.y), segments[12], 12)               // L
        drawSegment(segmentVertical(I.x, I.y), segments[9], 9)                 // I

    }
}


// Mapping of digits for 14 segments (A,B,C,D,E,F,G1,G2,H,I,J,K,L,M)
val digitSegmentMap = mapOf(
    0 to listOf(true, true, true, true, true, true, false, false, false, false, false, false, false, false),
    1 to listOf(false, true, true, false, false, false, false, false, false, false, false, false, false, false),
    2 to listOf(true, true, false, true, false, false, true, true, false, false, false, false, false, false),
    3 to listOf(true, true, true, true, false, false, false, true, false, false, false, false, false, false),
    4 to listOf(false, true, true, false, false, true, true, true, false, false, false, false, false, false),
    5 to listOf(true, false, true, true, false, true, true, true, false, false, false, false, false, false),
    6 to listOf(true, false, true, true, true, true, true, true, false, false, false, false, false, false),
    7 to listOf(true, true, true, false, false, false, false, false, false, false, false, false, false, false),
    8 to listOf(true, true, true, true, true, true, true, true, false, false, false, false, false, false),
    9 to listOf(true, true, true, true, false, true, true, true, false, false, false, false, false, false)
)

val charSegmentMap = mapOf(
    'A' to listOf(true, true, true, false, true, true, true, true, false, false, false, false, false, false),
    'B' to listOf(true, true, true, true, false, false, false, true, false, true, false, false, true, false),
    'C' to listOf(true, false, false, true, true, true, false, false, false, false, false, false, false, false),
    'D' to listOf(true, true, true, true, false, false, false, false, false, true, false, false, true, false),
    'E' to listOf(true, false, false, true, true, true, true, false, false, false, false, false, false, false),
    'F' to listOf(true, false, false, false, true, true, true, true, false, false, false, false, false, false),
    'G' to listOf(true, false, true, true, true, true, false, true, false, false, false, false, false, false),
    'H' to listOf(false, true, true, false, true, true, true, true, false, false, false, false, false, false),
    'I' to listOf(true, false, false, true, false, false, false, false, false, true, false, false, true, false),
    'J' to listOf(false, true, true, true, false, false, false, false, false, false, false, false, false, false),
    'K' to listOf(false, false, false, false, true, true, true, false, false, false, true, true, false, false),
    'L' to listOf(false, false, false, true, true, true, false, false, false, false, false, false, false, false),
    'M' to listOf(false, true, true, false, true, true, false, false, true, false, true, false, false, false),
    'N' to listOf(false, true, true, false, true, true, false, false, true, false, false, true, false, false),
    'O' to listOf(true, true, true, true, true, true, false, false, false, false, false, false, false, false),
    'P' to listOf(true, true, false, false, true, true, true, true, false, false, false, false, false, false),
    'Q' to listOf(true, true, true, true, true, true, false, false, false, false, false, true, false, false),
    'R' to listOf(true, true, false, false, true, true, true, true, false, false, false, true, false, false),
    'S' to listOf(true, false, true, true, false, true, true, true, false, false, false, false, false, false),
    'T' to listOf(true, false, false, false, false, false, false, false, false, true, false, false, true, false),
    'U' to listOf(false, true, true, true, true, true, false, false, false, false, false, false, false, false),
    'V' to listOf(false, false, false, false, true, true, false, false, false, false, true, false, false, true),
    'W' to listOf(false, true, true, false, true, true, false, false, false, false, false, true, false, true),
    'X' to listOf(false, false, false, false, false, false, false, false, true, false, true, true, false, true),
    'Y' to listOf(false, false, false, false, false, false, false, false, true, false, true, false, true, false),
    'Z' to listOf(true, false, false, true, false, false, false, false, false, false, true, false, false, true),

    'a' to listOf(false, false, false, true, true, false, true, false, false, false, false, false, true, false),
    'b' to listOf(false, false, false, true, true, true, true, false, false, false, false, true, false, false),
    'c' to listOf(false, false, false, true, true, false, true, true, false, false, false, false, false, false),
    'd' to listOf(false, true, true, true, false, false, false, true, false, false, false, false, false, true),
    'e' to listOf(false, false, false, true, true, false, true, false, false, false, false, false, false, true),
    'f' to listOf(false, false, false, false, false, false, true, true, false, false, true, false, true, false),
    'g' to listOf(false, true, true, true, false, false, false, true, false, false, true, false, false, false),
    'h' to listOf(false, false, true, false, false, false, false, true, false, true, false, false, true, false),
    'i' to listOf(false, false, false, false, false, false, false, false, false, false, false, false, true, false),
    'j' to listOf(false, false, false, false, true, false, false, false, false, true, false, false, false, true),
    'k' to listOf(false, false, false, false, false, false, false, false, false, true, true, true, true, false),
    'l' to listOf(false, false, false, false, true, true, false, false, false, false, false, false, false, false),
    'm' to listOf(false, false, true, false, true, false, true, true, false, false, false, false, true, false),
    'n' to listOf(false, false, false, false, true, false, true, false, false, false, false, false, true, false),
    'o' to listOf(false, false, true, true, true, false, true, true, false, false, false, false, false, false),
    'p' to listOf(false, false, false, false, true, true, true, false, true, false, false, false, false, false),
    'q' to listOf(false, true, true, false, false, false, false, true, false, false, true, false, false, false),
    'r' to listOf(false, false, false, false, true, false, true, false, false, false, false, false, false, false),
    's' to listOf(false, false, false, true, false, false, false, true, false, false, false, true, false, false),
    't' to listOf(false, false, false, true, true, true, true, false, false, false, false, false, false, false),
    'u' to listOf(false, false, true, true, true, false, false, false, false, false, false, false, false, false),
    'v' to listOf(false, false, false, false, true, false, false, false, false, false, false, false, false, true),
    'w' to listOf(false, false, true, false, true, false, false, false, false, false, false, true, false, true),
    'x' to listOf(false, false, false, false, false, false, false, false, true, false, true, true, false, true),
    'y' to listOf(false, true, true, true, false, false, false, true, false, true, false, false, false, false),
    'z' to listOf(false, false, false, true, false, false, true, false, false, false, false, false, false, true),



    // Quelques symboles courants
    '+' to listOf(false, false, false, false, false, false, true, true, false, true, false, false, true, false), // croix = tiret + vertical à définir selon dispo
    '=' to listOf(false, false, false, true, false, false, true, true, false, false, false, false, false, false), // égal G1 et G2
    '-' to listOf(false, false, false, false, false, false, true, true, false, false, false, false, false, false),
    '_' to listOf(false, false, false, true, false, false, false, false, false, false, false, false, false, false),
    '*' to listOf(false, false, false, false, false, false, false, false, true, true, true, true, true, true),


    '0' to listOf(true, true, true, true, true, true, false, false, false, false, true, false, false, true),
    '1' to listOf(false, true, true, false, false, false, false, false, false, false, true, false, false, false),
    '2' to listOf(true, true, false, true, true, false, true, true, false, false, false, false, false, false),
    '3' to listOf(true, true, true, true, false, false, false, true, false, false, false, false, false, false),
    '4' to listOf(false, true, true, false, false, true, true, true, false, false, false, false, false, false),
    '5' to listOf(true, false, true, true, false, true, true, true, false, false, false, false, false, false),
    '6' to listOf(true, false, true, true, true, true, true, true, false, false, false, false, false, false),
    '7' to listOf(true, true, true, false, false, false, false, false, false, false, false, false, false, false),
    '8' to listOf(true, true, true, true, true, true, true, true, false, false, false, false, false, false),
    '9' to listOf(true, true, true, true, false, true, true, true, false, false, false, false, false, false)
)