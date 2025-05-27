package com.lebaillyapp.sonicjammer

import android.os.Build
import android.os.Bundle
import android.util.MutableInt
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lebaillyapp.sonicjammer.composable.FiveDigitDisplay
import com.lebaillyapp.sonicjammer.composable.SevenSegmentDisplay
import com.lebaillyapp.sonicjammer.composable.SevenSegmentDisplayVariante
import com.lebaillyapp.sonicjammer.composable.SingleSegmentWithGlowAndGradientFlicker
import com.lebaillyapp.sonicjammer.ui.theme.SonicJammerTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setContent {

            var demoValue by remember { mutableIntStateOf(0) }

            LaunchedEffect(Unit) {
                while (true) {
                    delay(100)
                    // We'll increment demoValue up to 99999 to use all 5 digits
                    demoValue = (demoValue + 1) % 100000
                }
            }



            SonicJammerTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {

                    Column(modifier = Modifier.fillMaxSize()) {


                        // Extracting digits from demoValue
                        val digit1 = (demoValue / 10000) % 10
                        val digit2 = (demoValue / 1000) % 10
                        val digit3 = (demoValue / 100) % 10
                        val digit4 = (demoValue / 10) % 10
                        val digit5 = demoValue % 10

                        Spacer(modifier = Modifier.height(140.dp))
                        FiveDigitDisplay(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            digit1 = digit1,
                            digit2 = digit2,
                            digit3 = digit3,
                            digit4 = digit4,
                            digit5 = digit5,
                            digit1Color = Color(0xFF00E676),
                            digit2Color = Color(0xFF00E676),
                            digit3Color = Color(0xFF00E676),
                            digit4Color = Color(0xFFFFFFFF),
                            digit5Color = Color(0xFFFFFFFF),
                            digit1GlowRadius = 70f,
                            digit2GlowRadius = 70f,
                            digit3GlowRadius = 70f,
                            digit4GlowRadius = 70f,
                            digit5GlowRadius = 70f,
                            digit1FlickerAmplitude = 0.05f,
                            digit2FlickerAmplitude = 0.05f,
                            digit3FlickerAmplitude = 0.05f,
                            digit4FlickerAmplitude = 0.05f,
                            digit5FlickerAmplitude = 0.05f,
                            digit1FlickerFrequency = 1f,
                            digit2FlickerFrequency = 1f,
                            digit3FlickerFrequency = 1f,
                            digit4FlickerFrequency = 1.0f,
                            digit5FlickerFrequency = 1.0f,
                            offColor = Color(0xFF2A332F),
                            segmentLength = 9.dp,
                            segmentHorizontalLength = 9.dp,
                            segmentThickness = 3.dp,
                            bevel = 1.5.dp,
                            alpha = 1f,
                            spacer = 4.dp,
                        )

                        Spacer(modifier = Modifier.height(50.dp))

                        FiveDigitDisplay(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            digit1 = digit1,
                            digit2 = digit2,
                            digit3 = digit3,
                            digit4 = digit4,
                            digit5 = digit5,
                            digit1Color = Color(0xFF00F7FF),
                            digit2Color = Color(0xFF00E5FF),
                            digit3Color = Color(0xFF00E5FF),
                            digit4Color = Color(0xFF00E5FF),
                            digit5Color = Color(0xFFFF1744),
                            digit1GlowRadius = 70f,
                            digit2GlowRadius = 70f,
                            digit3GlowRadius = 70f,
                            digit4GlowRadius = 70f,
                            digit5GlowRadius = 70f,
                            digit1FlickerAmplitude = 0.05f,
                            digit2FlickerAmplitude = 0.15f,
                            digit3FlickerAmplitude = 0.10f,
                            digit4FlickerAmplitude = 0.20f,
                            digit5FlickerAmplitude = 0.1f,
                            digit1FlickerFrequency = 1f,
                            digit2FlickerFrequency = 0.5f,
                            digit3FlickerFrequency = 1f,
                            digit4FlickerFrequency = 1.0f,
                            digit5FlickerFrequency = 1.0f,
                            offColor = Color(0xFF2A332F),
                            segmentLength = 17.5.dp,
                            segmentHorizontalLength = 17.5.dp,
                            segmentThickness = 4.5.dp,
                            bevel = 2.dp,
                            alpha = 1f,
                            spacer = 10.dp,
                        )

                        Spacer(modifier = Modifier.height(50.dp))

                        FiveDigitDisplay(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            digit1 = 2,
                            digit2 = 4,
                            digit3 = 6,
                            digit4 = 3,
                            digit5 = 9,
                            digit1Color = Color(0xFFC6FF00),
                            digit2Color = Color(0xFFC6FF00),
                            digit3Color = Color(0xFFC6FF00),
                            digit4Color = Color(0xFFC6FF00),
                            digit5Color = Color(0xFF651FFF),
                            digit1GlowRadius = 50f,
                            digit2GlowRadius = 50f,
                            digit3GlowRadius = 50f,
                            digit4GlowRadius = 50f,
                            digit5GlowRadius = 50f,
                            digit1FlickerAmplitude = 0.05f,
                            digit2FlickerAmplitude = 0.15f,
                            digit3FlickerAmplitude = 0.10f,
                            digit4FlickerAmplitude = 0.20f,
                            digit5FlickerAmplitude = 0.1f,
                            digit1FlickerFrequency = 1f,
                            digit2FlickerFrequency = 0.5f,
                            digit3FlickerFrequency = 1f,
                            digit4FlickerFrequency = 1.0f,
                            digit5FlickerFrequency = 1.0f,
                            offColor = Color(0xFF2A332F),
                            segmentLength = 18.dp,
                            segmentHorizontalLength = 22.dp,
                            segmentThickness = 4.dp,
                            bevel = 2.dp,
                            alpha = 1f,
                            spacer = 6.dp,
                        )





                    }




                 }
            }
        }
    }
}

