package com.lebaillyapp.sonicjammer.oldStuff.composable.led

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sin
import kotlin.random.Random

import androidx.compose.animation.core.*

import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.sin

@Composable
fun LedBarGraph(
    modifier: Modifier = Modifier,
    numberOfLeds: Int = 10,
    targetPercentage: Float = 0f, // Le pourcentage cible pour l'animation
    ledHeight: Dp = 20.dp,
    ledWidth: Dp = 20.dp,
    spacing: Dp = 4.dp,
    ledColors: (Int) -> Color = { index -> // Fonction pour définir la couleur de chaque LED
        when {
            index < numberOfLeds * 0.4 -> Color(0xFF1DE9B6) // Les premières LEDs sont vertes
            index < numberOfLeds * 0.6 -> Color(0xFFFFA500) // Puis oranges
            else -> Color(0xFFFF1744) // Les dernières sont rouges
        }
    },
    offColor: Color = Color.DarkGray,
    alpha: Float = 1f,
    glowRadius: Float = 10f,
    flickerAmplitude: Float = 0.05f,
    flickerFrequency: Float = 5f,
    peakHoldEnabled: Boolean = true,
    peakHoldDurationMillis: Long = 1000, // Durée pendant laquelle la barre de peak est maintenue
    peakHoldFadeDurationMillis: Long = 300 // Durée de fondu de la barre de peak
) {
    val density = LocalDensity.current
    val ledWidthPx = with(density) { ledWidth.toPx() }
    val ledHeightPx = with(density) { ledHeight.toPx() }
    val spacingPx = with(density) { spacing.toPx() }

    // Animation du pourcentage
    val animatedPercentage by animateFloatAsState(
        targetValue = targetPercentage,
        animationSpec = tween(durationMillis = 150, easing = LinearEasing), // Animation rapide pour le vu-mètre
        label = "percentageAnimation"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "flickerTransition")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Restart),
        label = "timeAnimation"
    )

    // Pré-calculer les amplitudes de flicker pour chaque LED
    val flickerAlphas = remember {
        List(numberOfLeds) { Random(it).nextFloat() * 0.4f + 0.6f } // Base random pour chaque LED
    }

    fun flicker(index: Int, baseAlpha: Float = alpha): Float {
        val baseFlicker = flickerAlphas.getOrNull(index) ?: 1f
        return (baseAlpha * (baseFlicker + flickerAmplitude * sin(2 * Math.PI * flickerFrequency * time + index))).toFloat()
            .coerceIn(0f, 1f)
    }

    // --- Peak Hold Logic ---
    var currentPeakLevel by remember { mutableIntStateOf(0) }
    var peakFadeProgress by remember { mutableFloatStateOf(1f) } // 1f = fully opaque, 0f = fully transparent

    LaunchedEffect(animatedPercentage) {
        val newPeakLevel = (numberOfLeds * animatedPercentage).toInt().coerceIn(0, numberOfLeds)
        if (newPeakLevel > currentPeakLevel) {
            currentPeakLevel = newPeakLevel
            peakFadeProgress = 1f // Reset fade when a new peak is reached
        }
    }

    LaunchedEffect(currentPeakLevel) {
        if (peakHoldEnabled && currentPeakLevel > 0) {
            delay(peakHoldDurationMillis)
            val startTime = System.currentTimeMillis()
            while (true) {
                val elapsedTime = System.currentTimeMillis() - startTime
                val progress = (elapsedTime.toFloat() / peakHoldFadeDurationMillis).coerceIn(0f, 1f)
                peakFadeProgress = 1f - progress
                if (progress >= 1f) {
                    currentPeakLevel = 0 // Reset peak when fade is complete
                    break
                }
                delay(16) // ~60fps
            }
        }
    }
    // --- End Peak Hold Logic ---

    val totalHeightPx = (numberOfLeds * ledHeightPx) + ((numberOfLeds - 1) * spacingPx)
    val totalWidthPx = ledWidthPx

    Canvas(
        modifier = modifier
            .size(
                width = with(density) { totalWidthPx.toDp() },
                height = with(density) { totalHeightPx.toDp() }
            )
    ) {
        val paintGlow = Paint().apply {
            this.alpha = 255f
            this.asFrameworkPaint().maskFilter = BlurMaskFilter(glowRadius, BlurMaskFilter.Blur.NORMAL)
        }

        val numLedsOn = (numberOfLeds * animatedPercentage).toInt().coerceIn(0, numberOfLeds)

        for (i in 0 until numberOfLeds) {
            val isLedOn = i < numLedsOn
            val yPos = (numberOfLeds - 1 - i) * (ledHeightPx + spacingPx) // Empiler de bas en haut

            val ledColor = ledColors(i)

            // Dessiner la LED éteinte en arrière-plan
            drawRect(
                color = offColor.copy(alpha = alpha * 0.6f),
                topLeft = Offset(0f, yPos),
                size = Size(ledWidthPx, ledHeightPx)
            )

            // Dessiner la LED allumée (si elle l'est)
            if (isLedOn) {
                val currentAlpha = flicker(i)
                drawRect(
                    color = ledColor.copy(alpha = currentAlpha),
                    topLeft = Offset(0f, yPos),
                    size = Size(ledWidthPx, ledHeightPx)
                )

                // Dessiner le glow
                drawIntoCanvas {
                    paintGlow.color = ledColor // Le glow prend la couleur de la LED
                    it.nativeCanvas.drawRect(
                        0f, yPos, ledWidthPx, yPos + ledHeightPx,
                        paintGlow.asFrameworkPaint()
                    )
                }
            }
        }

        // Dessiner la barre de Peak Hold
        if (peakHoldEnabled && currentPeakLevel > 0) {
            val peakIndex = currentPeakLevel - 1 // La LED du haut du peak
            val peakYPos = (numberOfLeds - 1 - peakIndex) * (ledHeightPx + spacingPx)

            // Assurer que la couleur du peak est celle de la LED au niveau du peak
            val peakColor = ledColors(peakIndex)
            val peakAlpha = peakFadeProgress * alpha // Appliquer le fondu

            // Dessiner la LED de peak hold
            drawRect(
                color = peakColor.copy(alpha = peakAlpha),
                topLeft = Offset(0f, peakYPos),
                size = Size(ledWidthPx, ledHeightPx)
            )

            // Dessiner le glow pour la LED de peak hold
            drawIntoCanvas {
                paintGlow.color = peakColor
                paintGlow.alpha = (peakAlpha * 255).toInt().toFloat()
                it.nativeCanvas.drawRect(
                    0f, peakYPos, ledWidthPx, peakYPos + ledHeightPx,
                    paintGlow.asFrameworkPaint()
                )
            }
        }
    }
}