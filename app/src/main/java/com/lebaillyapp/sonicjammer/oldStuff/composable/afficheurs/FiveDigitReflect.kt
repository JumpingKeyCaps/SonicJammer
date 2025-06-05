package com.lebaillyapp.sonicjammer.oldStuff.composable.afficheurs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FiveDigitReflect(
    digit1: Int,
    digit2: Int,
    digit3: Int,
    digit4: Int,
    digit5: Int
){


    Column(modifier = Modifier.fillMaxSize()) {




        Spacer(modifier = Modifier.height(140.dp))



        FiveDigitDisplay(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            rotationYAngle = 0f,
            rotationXAngle = -2f,
            cameraAdjustment = 2f,
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
        Spacer(modifier = Modifier.height(0.dp))

        FiveDigitDisplay(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            rotationYAngle = 0f,
            rotationXAngle = 245f,
            cameraAdjustment = 6f,
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
            alpha = 0.2f,
            spacer = 10.dp,
        )







        Spacer(modifier = Modifier.height(50.dp))







    }



}
