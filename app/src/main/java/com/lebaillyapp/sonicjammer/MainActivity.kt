package com.lebaillyapp.sonicjammer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import com.lebaillyapp.sonicjammer.composable.afficheurs.FiveDigitReflect
import com.lebaillyapp.sonicjammer.composable.sevenSeg.SevenSegEvo
import com.lebaillyapp.sonicjammer.composable.sevenSeg.SevenSegmentDisplayExtended
import com.lebaillyapp.sonicjammer.config.SevenSegmentConfig
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
                    delay(10)
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

                    Spacer(modifier = Modifier.height(30.dp))


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
                            onColor = Color(0xFF1DE9B6)),
                        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
                            onColor = Color(0xFF1DE9B6)),
                        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
                            onColor = Color(0xFF1DE9B6)),
                        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
                            onColor = Color(0xFF1DE9B6)),
                        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
                            onColor = Color(0xFF1DE9B6)),
                        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
                            onColor = Color(0xFFFF9100)
                        ),
                        SevenSegmentConfig(segmentLength = segLgt,segmentHorizontalLength = segHorLgt, segmentThickness = segThk,bevel = bevel, offColor = offCol,flickerAmplitude = flickAmpl,flickerFrequency = flickFreq,glowRadius = glowRad,
                            onColor = Color(0xFFFF9100)),


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
                                segmentLength = 50.dp,
                                segmentHorizontalLength = 25.dp,
                                segmentThickness = 8.dp,
                                bevel = 4.dp,
                                onColor = Color(0xFFFF1744),
                                offColor = Color(0xFF2A332F),
                                alpha = 1f,
                                glowRadius = 70f,
                                flickerAmplitude = 0.05f,
                                flickerFrequency = 1f,
                                idleMode = false,
                                idleSpeed = 100
                        ),
                        nbrDigit = 7
                    )





                    Column {
                        DynamikRowAfficheur(configs = configlist,
                            overrideValue = "${demoValue*139}",
                            reversedOverride = true,
                            showZeroWhenEmpty = true,
                            activateReflect = false,
                            reflectAlpha = 0.3f,
                            reflectSpacing = 2.dp,
                            reflectAngle = 245f,
                            reflectGlowRadius = 10f,
                            reflectCameraAdjustment = 6f,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            extraSpacingStep = 2,
                            extraSpacing = 15.dp

                        )

                        Spacer(modifier = Modifier.height(80.dp))


                        DynamikRowAfficheur(configs = configlistB,
                            overrideValue = "$demoValue",
                            reversedOverride = true,
                            showZeroWhenEmpty = true,
                            activateReflect = true,
                            reflectAlpha = 0.3f,
                            reflectSpacing = 0.dp,
                            reflectAngle = 245f,
                            reflectGlowRadius = 35f,
                            reflectCameraAdjustment = 4f,
                            modifier = Modifier.align(Alignment.CenterHorizontally),

                        )

                        Spacer(modifier = Modifier.height(80.dp))


                        DynamikRowAfficheur(configs = configlistD,
                            overrideValue = "${demoValue*39}",
                            reversedOverride = true,
                            showZeroWhenEmpty = true,
                            activateReflect = false,
                            reflectAlpha = 0.3f,
                            reflectSpacing = 0.dp,
                            reflectAngle = 245f,
                            reflectGlowRadius = 35f,
                            reflectCameraAdjustment = 4f,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            )



                    }


                 }
            }
        }
    }
}


fun iteratorConfigGenerator(sevenSegCfg: SevenSegmentConfig,nbrDigit: Int): List<SevenSegmentConfig>{
    val list = mutableListOf<SevenSegmentConfig>()
    for (i in 1..nbrDigit){
        list.add(sevenSegCfg)
    }
    return list
}
