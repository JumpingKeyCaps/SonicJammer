package com.lebaillyapp.sonicjammer.oldStuff.screen

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lebaillyapp.sonicjammer.oldStuff.composable.FractalDisplay
import com.lebaillyapp.sonicjammer.oldStuff.composable.LissajousStyle
import com.lebaillyapp.sonicjammer.oldStuff.composable.SimpleNeonOscilloscope
import com.lebaillyapp.sonicjammer.oldStuff.composable.SimpleOscilloscopeConfig
import com.lebaillyapp.sonicjammer.oldStuff.composable.knob.RRKnobV2

import kotlin.math.PI
import kotlin.math.roundToInt

@Composable
fun DemoButtons() {
    Box(modifier = Modifier.fillMaxSize()) {

        var frequency by remember { mutableStateOf(17800f) }
        var isActive by remember { mutableStateOf(true) }
        var signalStrength by remember { mutableStateOf(0.4f) }
        var lissajousZoom by remember { mutableStateOf(1.0f) }

        var customlissajous_a by remember { mutableStateOf(1) }
        var customlissajous_b by remember { mutableStateOf(1) }
        var customlissajous_delta by remember { mutableStateOf(1) }

        var fovRation by remember { mutableStateOf(350) }
        var speedRot by remember { mutableStateOf(0.1f) }



        val oscillatorConfig = SimpleOscilloscopeConfig(
            neonColor = Color(0xFF1DE9B6),
            backgroundColor = Color(0xFF0A0A0A),
            gridColor = Color(0xFF1A4A1A),
            glowIntensity = 0.85f,
            scanlineIntensity = 0.3f
        )




        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(36.dp))
            SimpleNeonOscilloscope(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally)
                    .height(350.dp).clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color(0xFF181616), RoundedCornerShape(8.dp)),
                config = oscillatorConfig,
                frequency = frequency,
                isActive = isActive,
                signalStrength = signalStrength,
                lissajousZoom = lissajousZoom,
                figureStyle = LissajousStyle.CUSTOM,
                color1 = Color(0xFF1DE9B6),
                color2 = Color(0xFF1DE9B6).copy(alpha = 0.5f),
                customFov = fovRation.toFloat(),
                customRotationSpeed = speedRot,
                customA = customlissajous_a.toFloat(),
                customB = customlissajous_b.toFloat(),
                customDelta = PI.toFloat() / customlissajous_delta
            )
            Spacer(modifier = Modifier.height(36.dp))
            //Setting Knob ligne 1
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)){
                // zoom pour Lissajous
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text("Zoom : ${String.format("%.1f", lissajousZoom)}x", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.height(10.dp))
                    RRKnobV2(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        size = 75.dp,
                        steps = 32,
                        onValueChanged = { step ->
                            Log.d("Knob", "Position: $step")
                            lissajousZoom = step.toFloat() / 10
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
                Spacer(modifier = Modifier.width(30.dp))
                // a val Lissajous
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text("a : ${customlissajous_a}", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.height(10.dp))
                    RRKnobV2(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        size = 75.dp,
                        steps = 200,
                        onValueChanged = { step ->
                            customlissajous_a = step
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
                Spacer(modifier = Modifier.width(30.dp))
                // b val Lissajous
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text("b : ${customlissajous_b}", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.height(10.dp))
                    RRKnobV2(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        size = 75.dp,
                        steps = 200,
                        onValueChanged = { step ->
                            customlissajous_b = step
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
            Spacer(modifier = Modifier.height(20.dp))
            //Setting Knob ligne 2
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)){
                // delta lissajou
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text("Delta : ${customlissajous_delta}", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.height(10.dp))
                    RRKnobV2(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        size = 75.dp,
                        steps = 20,
                        onValueChanged = { step ->
                            customlissajous_delta = step
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
                Spacer(modifier = Modifier.width(30.dp))
                // fov lissajou
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text("Fov : ${fovRation}", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.height(10.dp))
                    RRKnobV2(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        size = 75.dp,
                        steps = 500,
                        onValueChanged = { step ->
                            fovRation = step
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
                Spacer(modifier = Modifier.width(30.dp))
                //  speed lissajou
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text("Speed : ${speedRot}", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.height(10.dp))
                    RRKnobV2(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        size = 75.dp,
                        steps = 100,
                        onValueChanged = { step ->
                            speedRot = step/1000f
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
            Spacer(modifier = Modifier.height(30.dp))
            // fractal deco
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(2.dp, Color(0xFF181616), RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0E0E0E)
                )
            ) {
                Box(modifier = Modifier.padding(0.dp).size(60.dp)){
                    FractalDisplay(config = oscillatorConfig, modifier = Modifier.align(Alignment.Center).padding(10.dp))
                }

            }

        }
    }
}