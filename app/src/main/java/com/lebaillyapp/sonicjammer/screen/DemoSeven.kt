package com.lebaillyapp.sonicjammer.screen

import android.graphics.BlurMaskFilter
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.sonicjammer.composable.afficheurs.DynamikRowAfficheur
import com.lebaillyapp.sonicjammer.composable.card.InnerShadowCard
import com.lebaillyapp.sonicjammer.composable.knob.RRKnobV2
import com.lebaillyapp.sonicjammer.composable.knob.RRKnobV2Limited
import com.lebaillyapp.sonicjammer.composable.led.LedBarGraph
import com.lebaillyapp.sonicjammer.config.SevenSegmentConfig
import com.lebaillyapp.sonicjammer.config.reflectConfig
import kotlin.math.roundToInt

@Composable
fun DemoSeven() {
    val numberOfLeds = 24      // Nombre de LEDs visibles
    val totalSteps = 13      // Nombre total de steps du knob (ex: 0..10)

    // Step ratio calculé dynamiquement pour atteindre 1.0 à max step
    val stepRatio = 1f / (totalSteps - 1)

    // Valeur actuelle du knob (simplifiée)
    var currentStep by remember { mutableStateOf(0) }

    // Calcul du volume actuel entre 0 et 1
    val currentVolume = (currentStep * stepRatio).coerceIn(0f, 1f)
    val currentVolumePercent = (currentVolume * 100).roundToInt()

    //reflection config
    val reflectionConfig = reflectConfig(
        reflectAlpha = 0.3f,
        reflectSpacing = 0.dp,
        reflectAngle = 245f,
        reflectGlowRadius = 35f,
        reflectCameraAdjustment = 4f
    )
    //general mutual config
    val configlist = listOf(
        SevenSegmentConfig(
            offColor = Color(0xFF2A1313),
            segmentLength = 12.5.dp,
            segmentHorizontalLength = 12.5.dp,
            segmentThickness = 2.5.dp,
            bevel = 1.5.dp,
            glowRadius = 35f,
            onColor = Color(0xFFF50057 )
        ),
        SevenSegmentConfig(
            offColor = Color(0xFF2A1313),
            segmentLength = 12.5.dp,
            segmentHorizontalLength = 12.5.dp,
            segmentThickness = 2.5.dp,
            bevel = 1.5.dp,
            glowRadius = 35f,
            onColor = Color(0xFFF50057 )
        ),
        SevenSegmentConfig(
            offColor = Color(0xFF2A1313),
            segmentLength = 12.5.dp,
            segmentHorizontalLength = 12.5.dp,
            segmentThickness = 2.5.dp,
            bevel = 1.5.dp,
            glowRadius = 35f,
            onColor = Color(0xFFF50057 )
        ),

    )

    Box(modifier = Modifier.fillMaxSize()){

        InnerShadowCard(
            modifier = Modifier
                .padding(0.dp)
                .align(Alignment.Center),
            shadowBlur = 2.dp
        ) {
            Column(modifier = Modifier.align(Alignment.Center).padding(20.dp)) {
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {

                    LedBarGraph(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        numberOfLeds = numberOfLeds,
                        targetPercentage = currentVolume,
                        peakHoldEnabled = true,
                        glowRadius = 35f,
                        offColor = Color(0xFF232525),
                        spacing = 2.dp,
                        ledWidth = 35.dp,
                        ledHeight = 4.dp,
                        flickerAmplitude = 0.25f,
                        flickerFrequency = 1f,
                        peakHoldDurationMillis = 700,
                        peakHoldFadeDurationMillis = 400
                    )

                    Spacer(modifier = Modifier.size(25.dp))

                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {

                        DynamikRowAfficheur(configs = configlist,
                            reflectConfig = reflectionConfig,
                            overrideValue = "${currentVolumePercent}",
                            reversedOverride = true,
                            showZeroWhenEmpty = true,
                            activateReflect = false,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.size(30.dp))

                        RRKnobV2Limited(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            size = 60.dp,
                            steps = totalSteps,
                            minValue = 0,
                            maxValue = totalSteps - 1,
                            initialValue = 0,
                            onValueChanged = { newValue ->
                                currentStep = newValue
                                Log.d("DemoSeven", "Knob value: $newValue, volume: ${currentVolume}")
                            },
                            showBackground = false,
                            backgroundColor = Color(0x741A1A1A),
                            backgroundShadowColor = Color.Black.copy(alpha = 0.2f),
                            tickColor = Color(0xFF4B4A4A),
                            activeTickColor = Color(0xFFFF6B35),
                            tickLength = 4.dp,
                            tickWidth = 2.dp,
                            tickSpacing = 12.dp,
                            indicatorColor = Color(0xFFFFA500),
                            indicatorSecondaryColor = Color(0xAAFF8C00),
                            bevelSizeRatio = 0.80f,
                            knobSizeRatio = 0.65f,
                        )
                    }
                }
            }
        }
    }

}

