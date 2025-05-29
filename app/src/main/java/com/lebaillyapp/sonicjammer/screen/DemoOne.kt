package com.lebaillyapp.sonicjammer.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lebaillyapp.sonicjammer.composable.afficheurs.DynamikRowAfficheur
import com.lebaillyapp.sonicjammer.config.SevenSegmentConfig
import com.lebaillyapp.sonicjammer.config.reflectConfig
import com.lebaillyapp.sonicjammer.iteratorConfigGenerator
import kotlinx.coroutines.delay

@Composable
fun DemoOne(){

    var demoValue by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(10)
            // We'll increment demoValue up to 99999 to use all 5 digits
            demoValue = (demoValue + 1) % 100000
        }
    }


    // general mutual config
    val segLgt = 15.dp
    val segHorLgt = 15.dp
    val segThk = 3.dp
    val bevel = 1.dp
    val offCol = Color(0xFF211D1D)
    val flickAmpl = 0.25f
    val flickFreq = 2f
    val glowRad = 20f

    //Config list
    val configlist = listOf(

        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
            onColor = Color(0xFF1DE9B6)
        ),
        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
            onColor = Color(0xFF1DE9B6)
        ),
        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
            onColor = Color(0xFF1DE9B6)
        ),
        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
            onColor = Color(0xFF1DE9B6)
        ),
        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
            onColor = Color(0xFF1DE9B6)
        ),
        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
            onColor = Color(0xFF1DE9B6)
        ),
        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
            onColor = Color(0xFFFF9100)
        ),
        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
            onColor = Color(0xFFFF9100)
        ),


        )


    val configlistB = listOf(
        SevenSegmentConfig(offColor = Color(0xFF2A1313),segmentLength = 17.5.dp,segmentHorizontalLength = 17.5.dp, segmentThickness = 4.5.dp,bevel = 2.dp,glowRadius = 70f, onColor = Color(0xFF00E5FF) ),
        SevenSegmentConfig(offColor = Color(0xFF2A1313),segmentLength = 17.5.dp,segmentHorizontalLength = 17.5.dp, segmentThickness = 4.5.dp,bevel = 2.dp,glowRadius = 70f, onColor = Color(0xFF00E5FF) ),
        SevenSegmentConfig(offColor = Color(0xFF2A1313),segmentLength = 17.5.dp,segmentHorizontalLength = 17.5.dp, segmentThickness = 4.5.dp,bevel = 2.dp,glowRadius = 70f, onColor = Color(0xFF00E5FF) ),
        SevenSegmentConfig(offColor = Color(0xFF2A1313),segmentLength = 17.5.dp,segmentHorizontalLength = 17.5.dp, segmentThickness = 4.5.dp,bevel = 2.dp,glowRadius = 70f, onColor = Color(0xFF00E5FF) ),
        SevenSegmentConfig(offColor = Color(0xFF2A1313),segmentLength = 17.5.dp,segmentHorizontalLength = 17.5.dp, segmentThickness = 4.5.dp,bevel = 2.dp,glowRadius = 70f, onColor = Color(0xFF00E5FF) ),
        SevenSegmentConfig(offColor = Color(0xFF2A1313),segmentLength = 17.5.dp,segmentHorizontalLength = 17.5.dp, segmentThickness = 4.5.dp,bevel = 2.dp,glowRadius = 70f, onColor = Color(
            0xFFFF1744
        )
        ),
    )


    val configlistD = iteratorConfigGenerator(
        sevenSegCfg = SevenSegmentConfig(
            digit = 1,
            char = null,
            manualSegments = null,
            segmentLength = 80.dp,
            segmentHorizontalLength = 30.dp,
            segmentThickness = 5.dp,
            bevel = 2.dp,
            onColor = Color(0xFFFFFFFF),
            offColor = Color(0xFF2A332F),
            alpha = 1f,
            glowRadius = 4f,
            flickerAmplitude = 0.05f,
            flickerFrequency = 1f,
            idleMode = false,
            idleSpeed = 100
        ),
        nbrDigit = 4
    )



    //reflection config
    val reflectionConfig = reflectConfig(
        reflectAlpha = 0.3f,
        reflectSpacing = 0.dp,
        reflectAngle = 245f,
        reflectGlowRadius = 35f,
        reflectCameraAdjustment = 4f
    )

    Column {
        DynamikRowAfficheur(configs = configlist,
            reflectConfig = reflectionConfig,
            overrideValue = "${demoValue*139}",
            reversedOverride = true,
            showZeroWhenEmpty = true,
            activateReflect = false,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            extraSpacingStep = 2,
            extraSpacing = 15.dp

        )

        Spacer(modifier = Modifier.height(80.dp))
        DynamikRowAfficheur(configs = configlistB,
            reflectConfig = reflectionConfig,
            overrideValue = "$demoValue",
            reversedOverride = true,
            showZeroWhenEmpty = true,
            activateReflect = true,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(80.dp))
        DynamikRowAfficheur(configs = configlistD,
            reflectConfig = reflectionConfig,
            overrideValue = "${demoValue*1}",
            reversedOverride = true,
            showZeroWhenEmpty = true,
            activateReflect = false,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

    }


}