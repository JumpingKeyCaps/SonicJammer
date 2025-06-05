package com.lebaillyapp.sonicjammer.oldStuff.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lebaillyapp.sonicjammer.oldStuff.composable.afficheurs.DynamikRowAfficheur
import com.lebaillyapp.sonicjammer.oldStuff.composable.fourteenSeg.FourteenSegmentDisplayExtended
import com.lebaillyapp.sonicjammer.oldStuff.composable.led.LedBarGraph
import com.lebaillyapp.sonicjammer.oldStuff.composable.visualizer.WaveformVisualizer
import com.lebaillyapp.sonicjammer.oldStuff.config.FourteenSegmentConfig
import com.lebaillyapp.sonicjammer.oldStuff.config.SevenSegmentConfig
import com.lebaillyapp.sonicjammer.oldStuff.config.reflectConfig

import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun DemoSix(){


    // Idle animation states
    val idle1 = listOf(false, false, false, false, false, false, false, false,  true, false, true, true, false, true)
    val idle2 = listOf(false, false, false, false, false, false, true, true,  false, false, false, false, false, false)
    val idleSegAnim = listOf(idle1, idle2)
    var spinspin by remember { mutableIntStateOf(0) }
    var currentSpechar by remember { mutableStateOf(idle1) }


    val allowedChars = (
            ('A'..'Z') +
                    ('0'..'9')
            ).toList()

    var currentChar by remember { mutableStateOf( 'h') }
    var currentCharB by remember { mutableStateOf('z') }


    var TxtBis by remember { mutableStateOf("JAMMING ") }


    LaunchedEffect(Unit) {
        while(true) {
            delay(200) // délai 500 ms
          //  currentChar = allowedChars.random()
         //   currentCharB = allowedChars.random()

            spinspin = (spinspin + 1) % idleSegAnim.size
            currentSpechar = idleSegAnim[spinspin]

        }
    }







    val config14segB = FourteenSegmentConfig(
        digit = null,
        char = null,
        manualSegments = null,
        segmentLength = 21.5.dp,
        segmentHorizontalLength = 13.5.dp,
        segmentThickness = 3.5.dp,
        bevel = 2.dp,
        onColor = Color(0xFF874EFF),
        offColor = Color(0xFF232525),
        alpha = 1f,
        glowRadius = 35f,
        flickerAmplitude = 0.25f,
        flickerFrequency = 4f,
        idleMode = false,
        idleSpeed = 100
    )
    val config14segSPE = FourteenSegmentConfig(
        digit = null,
        char = null,
        manualSegments = null,
        segmentLength = 10.dp,
        segmentHorizontalLength = 7.dp,
        segmentThickness = 2.dp,
        bevel = 1.dp,
        onColor = Color(0xFFFF1744),
        offColor = Color(0xFF232525),
        alpha = 1f,
        glowRadius = 35f,
        flickerAmplitude = 0.25f,
        flickerFrequency = 0.01f,
        idleMode = false,
        idleSpeed = 100
    )


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
            offColor = Color(0xFF3B3535),
            segmentLength = 19.5.dp,
            segmentHorizontalLength = 13.5.dp,
            segmentThickness = 4.5.dp,
            bevel = 2.dp,
            glowRadius = 35f,
            onColor = Color(0xFFC6FF00)
        ),
        SevenSegmentConfig(
            offColor = Color(0xFF3B3535),
            segmentLength = 19.5.dp,
            segmentHorizontalLength = 13.5.dp,
            segmentThickness = 4.5.dp,
            bevel = 2.dp,
            glowRadius = 35f,
            onColor = Color(0xFFC6FF00)
        ),
        SevenSegmentConfig(
            offColor = Color(0xFF3B3535),
            segmentLength = 19.5.dp,
            segmentHorizontalLength = 13.5.dp,
            segmentThickness = 4.5.dp,
            bevel = 2.dp,
            glowRadius = 35f,
            onColor = Color(0xFFC6FF00)
        ),
        SevenSegmentConfig(
            offColor = Color(0xFF3B3535),
            segmentLength = 19.5.dp,
            segmentHorizontalLength = 13.5.dp,
            segmentThickness = 4.5.dp,
            bevel = 2.dp,
            glowRadius = 35f,
            onColor = Color(0xFFC6FF00)
        ),
        SevenSegmentConfig(
            offColor = Color(0xFF3B3535),
            segmentLength = 19.5.dp,
            segmentHorizontalLength = 13.5.dp,
            segmentThickness = 4.5.dp,
            bevel = 2.dp,
            glowRadius = 35f,
            onColor = Color(0xFFC6FF00)
        ),
    )


    var currentVolume by remember { mutableStateOf(0.0f) }

    // --- LaunchedEffect de Debug avec pourcentage aléatoire ---
    LaunchedEffect(Unit) {
        while (true) {
            // Génère un pourcentage aléatoire entre 0.0f et 1.0f
            currentVolume = Random.nextFloat()
            delay(100) // Met à jour le volume toutes les 100ms
        }
    }



    Column {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            FourteenSegmentDisplayExtended(
                modifier = Modifier.align(Alignment.CenterVertically),
                config = config14segSPE,
                char = TxtBis[0]
            )
            Spacer(modifier = Modifier.size(5.dp))
            FourteenSegmentDisplayExtended(
                modifier = Modifier.align(Alignment.CenterVertically),
                config = config14segSPE,
                char = TxtBis[1]
            )
            Spacer(modifier = Modifier.size(5.dp))
            FourteenSegmentDisplayExtended(
                modifier = Modifier.align(Alignment.CenterVertically),
                config = config14segSPE,
                char = TxtBis[2]
            )
            Spacer(modifier = Modifier.size(5.dp))
            FourteenSegmentDisplayExtended(
                modifier = Modifier.align(Alignment.CenterVertically),
                config = config14segSPE,
                char = TxtBis[3]
            )
            Spacer(modifier = Modifier.size(5.dp))
            FourteenSegmentDisplayExtended(
                modifier = Modifier.align(Alignment.CenterVertically),
                config = config14segSPE,
                char = TxtBis[4]
            )
            Spacer(modifier = Modifier.size(5.dp))
            FourteenSegmentDisplayExtended(
                modifier = Modifier.align(Alignment.CenterVertically),
                config = config14segSPE,
                char = TxtBis[5]
            )
            Spacer(modifier = Modifier.size(5.dp))
            FourteenSegmentDisplayExtended(
                modifier = Modifier.align(Alignment.CenterVertically),
                config = config14segSPE,
                char = TxtBis[6]
            )
            Spacer(modifier = Modifier.size(5.dp))
            FourteenSegmentDisplayExtended(
                modifier = Modifier.align(Alignment.CenterVertically),
                config = config14segSPE,
                char = TxtBis[7]
            )
            Spacer(modifier = Modifier.size(5.dp))
            FourteenSegmentDisplayExtended(
                modifier = Modifier.align(Alignment.CenterVertically),
                config = config14segSPE,
                manualSegments = currentSpechar
            )
            Spacer(modifier = Modifier.size(5.dp))
            FourteenSegmentDisplayExtended(
                modifier = Modifier.align(Alignment.CenterVertically),
                config = config14segSPE,
                manualSegments = currentSpechar
            )
            Spacer(modifier = Modifier.size(5.dp))
            FourteenSegmentDisplayExtended(
                modifier = Modifier.align(Alignment.CenterVertically),
                config = config14segSPE,
                manualSegments = currentSpechar
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {


            DynamikRowAfficheur(configs = configlist,
                reflectConfig = reflectionConfig,
                overrideValue = "44100",
                reversedOverride = true,
                showZeroWhenEmpty = true,
                activateReflect = false,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.size(15.dp))


            FourteenSegmentDisplayExtended(
                modifier = Modifier.align(Alignment.CenterVertically),
                config = config14segB,
                char = currentChar
            )
            Spacer(modifier = Modifier.size(5.dp))
            FourteenSegmentDisplayExtended(
                modifier = Modifier.align(Alignment.CenterVertically),
                config = config14segB,
                char = currentCharB
            )

        }
        Spacer(modifier = Modifier.height(40.dp))


        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            LedBarGraph(
                numberOfLeds = 10,
                targetPercentage = currentVolume,
                peakHoldEnabled = true,
                glowRadius = 35f,
                offColor = Color(0xFF232525),
                spacing = 2.dp,
                ledWidth = 40.dp,
                ledHeight = 4.dp,
                flickerAmplitude = 0.25f,
                flickerFrequency = 1f,
                peakHoldDurationMillis = 700,
                peakHoldFadeDurationMillis = 400
            )
            Spacer(modifier = Modifier.size(15.dp))
            WaveformVisualizer(
                frequencyHz = 17500f,
                factorVisualizer = 130f,
                modifier = Modifier
                    .width(150.dp)
                    .height(65.dp),
                amplitude = 0.8f,
                waveformColor = Color(0xFFFF1744),
                visualWindowDurationMs = 8f,
                visualResolutionPointsPerMs = 25f
            )
        }





    }

}