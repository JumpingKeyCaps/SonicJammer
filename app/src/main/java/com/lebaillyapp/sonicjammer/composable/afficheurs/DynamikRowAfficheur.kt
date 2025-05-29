package com.lebaillyapp.sonicjammer.composable.afficheurs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.sonicjammer.composable.sevenSeg.SevenSegmentDisplayExtended
import com.lebaillyapp.sonicjammer.config.SevenSegmentConfig
import com.lebaillyapp.sonicjammer.config.reflectConfig

@Composable
fun DynamikRowAfficheur(
    modifier: Modifier = Modifier,
    configs: List<SevenSegmentConfig>,
    overrideValue: String? = null,
    reversedOverride: Boolean = false,
    spacing: Dp = 8.dp,
    showZeroWhenEmpty: Boolean = false,
    extraSpacingStep: Int = 0, // tous les N afficheurs (0 = désactivé)
    extraSpacing: Dp = 0.dp,
    activateReflect: Boolean = false,
    reflectConfig: reflectConfig
) {
    Column(modifier = modifier) {
        //Affichage des chiffres
        Row(
            modifier = modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            configs.forEachIndexed { index, config ->
                if (index > 0) {
                    val addExtra = extraSpacingStep > 0 && index % extraSpacingStep == 0
                    val totalSpacing = spacing + if (addExtra) extraSpacing else 0.dp
                    Spacer(modifier = Modifier.width(totalSpacing))
                }

                val overrideChar = when {
                    overrideValue == null -> null

                    reversedOverride -> {
                        val charIndex = overrideValue.length - configs.size + index
                        overrideValue.getOrNull(charIndex)
                    }

                    else -> overrideValue.getOrNull(index)
                }

                val digitOverride = overrideChar?.digitToIntOrNull()
                val charOverride = if (digitOverride == null) overrideChar else null

                val displayDigit = when {
                    overrideChar != null -> digitOverride
                    showZeroWhenEmpty    -> 0
                    else                 -> null
                }

                val displayChar = when {
                    overrideChar != null -> charOverride
                    showZeroWhenEmpty    -> null
                    else                 -> null
                }

                val displayManual = if (overrideChar == null && !showZeroWhenEmpty) {
                    List(7) { false }
                } else config.manualSegments

                SevenSegmentDisplayExtended(
                    digit = displayDigit ?: config.digit,
                    char = displayChar ?: config.char,
                    manualSegments = displayManual,
                    segmentLength = config.segmentLength,
                    segmentHorizontalLength = config.segmentHorizontalLength,
                    segmentThickness = config.segmentThickness,
                    bevel = config.bevel,
                    onColor = config.onColor,
                    offColor = config.offColor,
                    alpha = config.alpha,
                    glowRadius = config.glowRadius,
                    flickerAmplitude = config.flickerAmplitude,
                    flickerFrequency = config.flickerFrequency,
                    idleMode = config.idleMode,
                    idleSpeed = config.idleSpeed
                )
            }
        }

        //REFLEXION
        if (activateReflect) {
            Spacer(modifier = Modifier.height(reflectConfig.reflectSpacing))
            Row(
                modifier = modifier.alpha(reflectConfig.reflectAlpha).graphicsLayer {
                    rotationY = 0f
                    rotationX = reflectConfig.reflectAngle
                    cameraDistance = reflectConfig.reflectCameraAdjustment * density
                }.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(spacing))
                configs.forEachIndexed { index, config ->
                    if (index > 0) {
                        val addExtra = extraSpacingStep > 0 && index % extraSpacingStep == 0
                        val totalSpacing = spacing + if (addExtra) extraSpacing else 0.dp
                        Spacer(modifier = Modifier.width(totalSpacing))
                    }

                    val overrideChar = when {
                        overrideValue == null -> null

                        reversedOverride -> {
                            val charIndex = overrideValue.length - configs.size + index
                            overrideValue.getOrNull(charIndex)
                        }

                        else -> overrideValue.getOrNull(index)
                    }

                    val digitOverride = overrideChar?.digitToIntOrNull()
                    val charOverride = if (digitOverride == null) overrideChar else null

                    val displayDigit = when {
                        overrideChar != null -> digitOverride
                        showZeroWhenEmpty    -> 0
                        else                 -> null
                    }

                    val displayChar = when {
                        overrideChar != null -> charOverride
                        showZeroWhenEmpty    -> null
                        else                 -> null
                    }

                    val displayManual = if (overrideChar == null && !showZeroWhenEmpty) {
                        List(7) { false }
                    } else config.manualSegments

                    SevenSegmentDisplayExtended(
                        digit = displayDigit ?: config.digit,
                        char = displayChar ?: config.char,
                        manualSegments = displayManual,
                        segmentLength = config.segmentLength,
                        segmentHorizontalLength = config.segmentHorizontalLength,
                        segmentThickness = config.segmentThickness,
                        bevel = config.bevel,
                        onColor = config.onColor,
                        offColor = config.offColor,
                        alpha = config.alpha,
                        glowRadius = reflectConfig.reflectGlowRadius,
                        flickerAmplitude = config.flickerAmplitude,
                        flickerFrequency = config.flickerFrequency,
                        idleMode = config.idleMode,
                        idleSpeed = config.idleSpeed
                    )
                }
                Spacer(modifier = Modifier.width(spacing))
            }
        }


    }
}