package com.lebaillyapp.sonicjammer.screen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.sonicjammer.composable.afficheurs.DynamikRowAfficheur
import com.lebaillyapp.sonicjammer.composable.knob.RRKnob
import com.lebaillyapp.sonicjammer.composable.knob.RRKnobV2
import com.lebaillyapp.sonicjammer.composable.knob.RealisticRotaryKnobBlacked
import com.lebaillyapp.sonicjammer.composable.led.RealisticLED
import com.lebaillyapp.sonicjammer.composable.visualizer.WaveformVisualizer
import com.lebaillyapp.sonicjammer.config.SevenSegmentConfig
import com.lebaillyapp.sonicjammer.config.reflectConfig
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun DemoFour(){


    //simulation debug
    var demoValue by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(10)
           // demoValue = (demoValue + 1) % 100000
        }
    }


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
            segmentLength = 17.5.dp,
            segmentHorizontalLength = 17.5.dp,
            segmentThickness = 4.5.dp,
            bevel = 2.dp,
            glowRadius = 35f,
            onColor = Color(
            0xFF1DE9B6
        )
        ),
        SevenSegmentConfig(offColor = Color(0xFF2A1313),segmentLength = 17.5.dp,segmentHorizontalLength = 17.5.dp, segmentThickness = 4.5.dp,bevel = 2.dp,glowRadius = 35f, onColor = Color(
            0xFF1DE9B6
        )
        ),
        SevenSegmentConfig(offColor = Color(0xFF2A1313),segmentLength = 17.5.dp,segmentHorizontalLength = 17.5.dp, segmentThickness = 4.5.dp,bevel = 2.dp,glowRadius = 35f, onColor = Color(
            0xFF1DE9B6
        )
        ),
        SevenSegmentConfig(offColor = Color(0xFF2A1313),segmentLength = 17.5.dp,segmentHorizontalLength = 17.5.dp, segmentThickness = 4.5.dp,bevel = 2.dp,glowRadius = 35f, onColor = Color(
            0xFF1DE9B6
        )
        ),
        SevenSegmentConfig(offColor = Color(0xFF2A1313),segmentLength = 17.5.dp,segmentHorizontalLength = 17.5.dp, segmentThickness = 4.5.dp,bevel = 2.dp,glowRadius = 35f, onColor = Color(
            0xFF1DE9B6
        )
        ),
        SevenSegmentConfig(offColor = Color(0xFF2A1313),segmentLength = 17.5.dp,segmentHorizontalLength = 17.5.dp, segmentThickness = 4.5.dp,bevel = 2.dp,glowRadius = 35f, onColor = Color(
            0xFF1DE9B6
        )
        ),
        SevenSegmentConfig(offColor = Color(0xFF2A1313),segmentLength = 17.5.dp,segmentHorizontalLength = 17.5.dp, segmentThickness = 4.5.dp,bevel = 2.dp,glowRadius = 35f, onColor = Color(
            0xFFFF1744
        )
        ),
        SevenSegmentConfig(offColor = Color(0xFF2A1313),segmentLength = 17.5.dp,segmentHorizontalLength = 17.5.dp, segmentThickness = 4.5.dp,bevel = 2.dp,glowRadius = 35f, onColor = Color(
            0xFFFF1744
        )
        ),
    )




    Column {
        DynamikRowAfficheur(configs = configlist,
            reflectConfig = reflectionConfig,
            overrideValue = "$demoValue",
            reversedOverride = true,
            showZeroWhenEmpty = true,
            activateReflect = true,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))






        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            RealisticRotaryKnobBlacked(
                size = 100.dp,
                steps = 8,
                onValueChanged = { step ->
                    Log.d("Knob", "Position: $step")
                    demoValue++
                }
            )
            Spacer(modifier = Modifier.width(50.dp))
            RealisticRotaryKnobBlacked(
                modifier = Modifier.align(Alignment.CenterVertically),
                size = 90.dp,
                steps = 16,
                onValueChanged = { step ->
                    Log.d("Knob", "Position: $step")
                    demoValue-=100
                }
            )

        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            RealisticLED(
                modifier = Modifier.align(Alignment.CenterVertically),
                size = 25f,
                isOn = true,
                color = Color(0xFF1DE9B6),
                blinkInterval = 1000,
                haloSpacer = 3
            )
            Spacer(modifier = Modifier.width(10.dp))
            RealisticLED(
                modifier = Modifier.align(Alignment.CenterVertically),
                size = 25f,
                isOn = true,
                color = Color(0xFFFFFFFF),
                blinkInterval = 100,
                haloSpacer = 3
            )
        }



        Spacer(modifier = Modifier.width(50.dp))
        RRKnobV2(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            size = 250.dp,
            steps = 32,
            onValueChanged = { step ->
                Log.d("Knob", "Position: $step")
                demoValue+=100
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









