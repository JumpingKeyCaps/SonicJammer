package com.lebaillyapp.sonicjammer.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.lebaillyapp.sonicjammer.composable.afficheurs.DynamikRowAfficheur
import com.lebaillyapp.sonicjammer.config.SevenSegmentConfig
import com.lebaillyapp.sonicjammer.config.reflectConfig
import com.lebaillyapp.sonicjammer.iteratorConfigGenerator
import kotlinx.coroutines.delay

@Composable
fun DemoTwo(){

    var tirageCp1 by remember { mutableIntStateOf(0) }
    var tirage0 by remember { mutableIntStateOf(0) }
    var tirage1 by remember { mutableIntStateOf(0) }
    var tirage2 by remember { mutableIntStateOf(0) }
    var tirage3 by remember { mutableIntStateOf(0) }

    var idletrigger by remember { mutableStateOf(true) }

    var tirageTriggerKey by remember { mutableStateOf(0) }


    LaunchedEffect(tirageTriggerKey) {
        idletrigger = true
        delay(2000)


        tirage0 = (0..25).random()
        tirage1 = (0..25).random()
        tirage2 = (0..25).random()
        tirage3 = (0..25).random()


        val Tir1 = tirage0 + tirage1
        val Tir2 = tirage2 + tirage3

        if (Tir1 > Tir2){
            tirageCp1 = (tirage0 + tirage1) - (tirage2 + tirage3)
        }else{
            tirageCp1 = (tirage2 + tirage3) - (tirage0 + tirage1)
        }




        Log.d("tirage", "TIRAGE:  $tirageCp1")
        idletrigger = false


    }


    // big config
    val segLgt2 = 40.dp
    val segHorLgt2 = 30.dp
    val segThk2 = 7.dp
    val bevel2 = 4.dp
    val offCol2 = Color(0xFF332F21)
    val onCol2 = Color(0xFFFF1744)
    val flickAmpl2 = 0.15f
    val flickFreq2 = 1f
    val glowRad2 = 70f

    val configCompoBig = iteratorConfigGenerator(
        sevenSegCfg = SevenSegmentConfig(
            digit = null,
            char = null,
            manualSegments = null,
            segmentLength = segLgt2,
            segmentHorizontalLength = segHorLgt2,
            segmentThickness = segThk2,
            bevel = bevel2,
            onColor = onCol2,
            offColor = offCol2,
            alpha = 1f,
            glowRadius = glowRad2,
            flickerAmplitude = flickAmpl2,
            flickerFrequency = flickFreq2,
            idleMode = idletrigger,
            idleSpeed = 100
        ),
        nbrDigit = 2
    )



// general mutual config
    val segLgt = 15.dp
    val segHorLgt = 15.dp
    val segThk = 3.dp
    val bevel = 2.dp
    val offCol = Color(0xFF243321)
    val onCol = Color(0xFFFFC400)
    val flickAmpl = 0.25f
    val flickFreq = 2f
    val glowRad = 20f

    val configCompo = iteratorConfigGenerator(
        sevenSegCfg = SevenSegmentConfig(
            digit = null,
            char = null,
            manualSegments = null,
            segmentLength = 10.dp,
            segmentHorizontalLength = 10.dp,
            segmentThickness = 2.dp,
            bevel = 1.dp,
            onColor = onCol,
            offColor = offCol,
            alpha = 1f,
            glowRadius = glowRad,
            flickerAmplitude = flickAmpl,
            flickerFrequency = flickFreq,
            idleMode = idletrigger,
            idleSpeed = 100
        ),
        nbrDigit = 2
    )
    val configCompoB = iteratorConfigGenerator(
        sevenSegCfg = SevenSegmentConfig(
            digit = null,
            char = null,
            manualSegments = null,
            segmentLength = segLgt,
            segmentHorizontalLength = segHorLgt,
            segmentThickness = segThk,
            bevel = bevel,
            onColor = Color(0xFF1BFA99),
            offColor = offCol,
            alpha = 1f,
            glowRadius = glowRad,
            flickerAmplitude = flickAmpl,
            flickerFrequency = flickFreq,
            idleMode = idletrigger,
            idleSpeed = 100
        ),
        nbrDigit = 2
    )





    //reflection config
    val reflectionConfig = reflectConfig(
        reflectAlpha = 0.3f,
        reflectSpacing = 0.dp,
        reflectAngle = 245f,
        reflectGlowRadius = 35f,
        reflectCameraAdjustment = 4f
    )

    Box(Modifier.fillMaxSize().clickable { tirageTriggerKey++ }){

        Row(Modifier.align(Alignment.Center)){

            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                DynamikRowAfficheur(configs = configCompoBig,
                    reflectConfig = reflectionConfig,
                    overrideValue = "$tirageCp1",
                    reversedOverride = true,
                    showZeroWhenEmpty = true,
                    activateReflect = true,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    extraSpacingStep = 2,
                    extraSpacing = 15.dp
                )

            }
            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                DynamikRowAfficheur(configs = configCompoB,
                    reflectConfig = reflectionConfig,
                    overrideValue = "$tirage0",
                    reversedOverride = true,
                    showZeroWhenEmpty = true,
                    activateReflect = false,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    extraSpacingStep = 2,
                    extraSpacing = 15.dp
                )
                Spacer(modifier = Modifier.height(20.dp))
                DynamikRowAfficheur(configs = configCompoB,
                    reflectConfig = reflectionConfig,
                    overrideValue = "$tirage1",
                    reversedOverride = true,
                    showZeroWhenEmpty = true,
                    activateReflect = false,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    extraSpacingStep = 2,
                    extraSpacing = 15.dp
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                DynamikRowAfficheur(
                    configs = configCompo,
                    reflectConfig = reflectionConfig,
                    overrideValue = "$tirage2",
                    reversedOverride = true,
                    showZeroWhenEmpty = true,
                    activateReflect = false,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    extraSpacingStep = 2,
                    extraSpacing = 15.dp
                )
                Spacer(modifier = Modifier.height(20.dp))
                DynamikRowAfficheur(
                    configs = configCompo,
                    reflectConfig = reflectionConfig,
                    overrideValue = "$tirage3",
                    reversedOverride = true,
                    showZeroWhenEmpty = true,
                    activateReflect = false,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    extraSpacingStep = 2,
                    extraSpacing = 15.dp
                )
            }





        }
    }
}