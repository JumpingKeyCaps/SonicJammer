package com.lebaillyapp.sonicjammer.oldStuff.config

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class SevenSegmentConfig(
    val digit: Int? = null,
    val char: Char? = null,
    val manualSegments: List<Boolean>? = null,
    val segmentLength: Dp = 15.dp,
    val segmentHorizontalLength: Dp = 15.dp,
    val segmentThickness: Dp = 3.dp,
    val bevel: Dp = 1.dp,
    val onColor: Color = Color.Red,
    val offColor: Color = Color.DarkGray,
    val alpha: Float = 1f,
    val glowRadius: Float = 15f,
    val flickerAmplitude: Float = 0.25f,
    val flickerFrequency: Float = 1f,
    val idleMode: Boolean = false,
    val idleSpeed: Long = 100

)