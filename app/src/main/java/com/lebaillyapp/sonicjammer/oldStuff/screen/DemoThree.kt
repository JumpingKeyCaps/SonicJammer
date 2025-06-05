package com.lebaillyapp.sonicjammer.oldStuff.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.lebaillyapp.sonicjammer.oldStuff.composable.afficheurs.DynamikRowAfficheur
import com.lebaillyapp.sonicjammer.oldStuff.config.SevenSegmentConfig
import com.lebaillyapp.sonicjammer.oldStuff.config.reflectConfig

import kotlinx.coroutines.delay

@Composable
fun DemoThree(){

    //simulation debug
    var demoValue by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(10)
            demoValue = (demoValue + 1) % 100000
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
    )



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        contentAlignment = Alignment.Center
    ) {
        DisplayFrame(
            modifier = Modifier
        ) {
            DynamikRowAfficheur(configs = configlist,
                reflectConfig = reflectionConfig,
                overrideValue = "$demoValue",
                reversedOverride = true,
                showZeroWhenEmpty = true,
                activateReflect = true
            )
        }
    }
}

@Composable
fun DisplayFrame(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                shadowElevation = 8.dp.toPx()
                shape = RoundedCornerShape(10.dp)
                clip = true
            }
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0x27232323),
                        Color(0xFF1A1A1A),
                        Color(0x27232323)
                    )
                ),
                shape = RoundedCornerShape(7.dp)
            )
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1C1C1C),
                        Color(0xFF282727),
                        Color(0xFF1C1C1C)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(20.dp)
    ) {
        content()

        Canvas(modifier = Modifier.matchParentSize()) {
            drawArc(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.7f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.2f, size.height * 0.2f),
                    radius = size.minDimension * 0.7f
                ),
                startAngle = -60f,
                sweepAngle = 120f,
                useCenter = false,
                style = Stroke(width = 4.dp.toPx())
            )
        }
    }
}