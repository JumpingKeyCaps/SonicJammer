package com.lebaillyapp.sonicjammer.oldStuff.composable.sevenSeg

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SevenSegEvo(
    modifier: Modifier = Modifier,
    rotationYAngle: Float = 0f,
    rotationXAngle: Float = 0f,
    cameraAdjustment: Float = 4f,
    digit: Int = 0,
    digitColor: Color = Color.Red,
    digitGlowRadius: Float = 70f,
    digitReflectGlowRadius: Float = 70f,
    digitFlickerAmplitude: Float = 0.05f,
    digitFlickerFrequency: Float = 1f,
    offColor: Color = Color(0xFF252222),
    segmentLength: Dp = 17.5.dp,
    segmentHorizontalLength: Dp = 17.5.dp,
    segmentThickness: Dp = 4.5.dp,
    bevel: Dp = 2.dp,
    alpha: Float = 1f,
    alphaReflect: Float = 0.2f,
    reflectActivated: Boolean = false
){

    Column(modifier = modifier
        .graphicsLayer {
            rotationY = rotationYAngle
            rotationX = rotationXAngle
            cameraDistance = cameraAdjustment * density
        }
    ){
        SevenSegmentDisplayVariante(
            digit = digit,
            glowRadius = digitGlowRadius,
            flickerAmplitude = digitFlickerAmplitude,
            flickerFrequency = digitFlickerFrequency,
            alpha = alpha,
            segmentLength = segmentLength,
            segmentHorizontalLength = segmentHorizontalLength,
            segmentThickness = segmentThickness,
            bevel = bevel,
            onColor = digitColor,
            offColor = offColor,
            modifier = Modifier
        )
        if(reflectActivated){
            Box(modifier = Modifier
                .graphicsLayer {
                    rotationY = 0f
                    rotationX = 245f
                    cameraDistance = 4f * density
                }
            ){
                SevenSegmentDisplayVariante(
                    digit = digit,
                    glowRadius = digitReflectGlowRadius,
                    flickerAmplitude = digitFlickerAmplitude,
                    flickerFrequency = digitFlickerFrequency,
                    alpha = alphaReflect,
                    segmentLength = segmentLength,
                    segmentHorizontalLength = segmentHorizontalLength,
                    segmentThickness = segmentThickness,
                    bevel = bevel,
                    onColor = digitColor,
                    offColor = offColor,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        }
    }




}