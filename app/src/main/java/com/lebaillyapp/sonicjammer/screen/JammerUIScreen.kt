package com.lebaillyapp.sonicjammer.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lebaillyapp.sonicjammer.composition.ButtonRockerSwitchVertical
import com.lebaillyapp.sonicjammer.viewmodel.JamViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lebaillyapp.sonicjammer.R
import com.lebaillyapp.sonicjammer.composition.GlassButton
import com.lebaillyapp.sonicjammer.composition.RRStartStopButtonRect
import com.lebaillyapp.sonicjammer.composition.RealisticLED
import com.lebaillyapp.sonicjammer.composition.RepeatingRippleCyberCanvas
import com.lebaillyapp.sonicjammer.composition.SyncedDualModalSheet
import com.lebaillyapp.sonicjammer.composition.SyncedDualModalSheetGlassUp
import com.lebaillyapp.sonicjammer.oldStuff.composable.afficheurs.DynamikRowAfficheur
import com.lebaillyapp.sonicjammer.oldStuff.composable.knob.RRKnobV2
import com.lebaillyapp.sonicjammer.oldStuff.config.SevenSegmentConfig
import com.lebaillyapp.sonicjammer.oldStuff.config.reflectConfig
import com.lebaillyapp.sonicjammer.oldStuff.iteratorConfigGenerator
import kotlinx.coroutines.delay
import kotlin.random.Random


@Composable
fun JammerUIScreen(
    viewModel: JamViewModel  = hiltViewModel()
) {

    val jamConfig by viewModel.config.collectAsState()
    val isEmitting by viewModel.isPlaying.collectAsState()



    var isClassicJammerMode by remember { mutableStateOf(true) }
    var isVisible by remember { mutableStateOf(true) }

    //todo UI TEST ONLY - to replace by jamconfig from viewmodel
    var ampModFrequencyValue by remember { mutableStateOf(2.0) }
    var ampModDepthValue by remember { mutableStateOf(0.0) }// real val is divised by 10 (0.6)
    var chaosFrequencyValue by remember { mutableStateOf(0.0) }
    var chaosDepthValue by remember { mutableStateOf(0.0) }
    var burstProbabilityValue by remember { mutableStateOf(0f) }// real val is divised by 10 (0.3)
    var clipFactorValue by remember { mutableStateOf(0) }// real val is divised by 10 (1.2)
    var maxFreqValue by remember { mutableStateOf(18500.0) }
    var minFreqValue by remember { mutableStateOf(17500.0) }

    var freqDevValue by remember { mutableStateOf(500.0) }


    LaunchedEffect(
        ampModFrequencyValue,
        ampModDepthValue,
        chaosFrequencyValue,
        chaosDepthValue,
        burstProbabilityValue,
        clipFactorValue,
        minFreqValue,
        maxFreqValue,
        freqDevValue
    ) {
        viewModel.updateConfig(
            jamConfig.copy(
                modFreq = ampModFrequencyValue.toDouble(),
                modDepth = ampModDepthValue / 10.0,
                chaosFreq1 = chaosFrequencyValue.toDouble(),
                chaosDepth = chaosDepthValue.toDouble(),
                burstProbability = burstProbabilityValue / 10f,
                clipFactor = clipFactorValue / 10.0,
                minFreq = minFreqValue.toDouble(),
                maxFreq = maxFreqValue.toDouble(),
                freqDev = freqDevValue.toDouble()
            )
        )
    }




    // led activation
    var isLed1Active by remember { mutableStateOf(false) }
    var isLed2Active by remember { mutableStateOf(false) }
    var isLed3Active by remember { mutableStateOf(false) }
    var isLed4Active by remember { mutableStateOf(false) }


    LaunchedEffect(isEmitting) {
        while (isEmitting) {
            delay(100)
            isLed1Active = !isLed1Active
            delay(100)
            isLed2Active = !isLed2Active
            delay(100)
            isLed3Active = !isLed3Active
            delay(100)
            isLed4Active = !isLed4Active
            delay(100)
        }
        isLed1Active = false
        isLed2Active = false
        isLed3Active = false
        isLed4Active = false
    }


    //todo -------------------------------------------------------------

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A)) // temporaire pour debug visuel
    ) {


        // main visual
        RepeatingRippleCyberCanvas(
            modifier = Modifier.fillMaxSize()
                .clickable { isVisible = !isVisible },
            intervalMs = 1000,//1000
            speed = 0.99f,
            amplitude = 150f,
            gridSize = 45, //20 (lower = more dots so need good smartphone (galaxy S))
            isActive = isEmitting,
            colorBase = Color(0xFFF50057),//0xFFF50057
            colorAccent = Color(0xFFFF9100), // 0xFF1A181A
            radiusDots = 3.5f,
            frameRate = 42L
        )





        // dual modal for settings
        SyncedDualModalSheetGlassUp(
            visible = isVisible,
            onDismiss = { isVisible = false },
            scrimMaxAlpha = 0.2f,
            topSheetRatio = 0.40f,
            bottomSheetRatio = 0.60f,
            autoDismissThresholdRatio = 0.10f,
            modalBackgroundColor = Color(0xED050505),
            cornerRadius = 20.dp,
            peekHeight = 0.dp,
            topContent = {
                //top Modal : values
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(modifier = Modifier.padding(top = 10.dp).fillMaxWidth()) {
                        Image(
                            painter = painterResource(id = R.drawable.jamicon),
                            contentDescription = "logoapp",
                            modifier = Modifier.size(180.dp).align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop // ou Fit, FillBounds, etc.
                        )
                    }



                }
            },
            bottomContent = {
                //Bottom Modal : knobs
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Column(modifier = Modifier.padding(top = 0.dp).fillMaxWidth()) {

                        Spacer(Modifier.height(10.dp))

                        //buttons
                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {

                            RRStartStopButtonRect(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                width = 150.dp,
                                height = 40.dp,
                                isStarted = isClassicJammerMode,
                                onToggle = { isClassicJammerMode = !isClassicJammerMode },
                                startColor = Color(0xFFFFAB00),
                                stopColor = Color(0xFF00BFA5),
                                textA = "RAW",
                                textB = "SCO",
                                backgroundColor = Color(0xFF1A1A1A),
                                shadowColor = Color.Black.copy(alpha = 0.6f),
                                cornerRadius = 8.dp,
                                glowRadius = 3.dp
                            )
                            Spacer(Modifier.width(20.dp))

                            //Start/Stop button
                            RRStartStopButtonRect(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                width = 150.dp,
                                height = 40.dp,
                                isStarted = isEmitting,
                                onToggle = {
                                    if (isEmitting) {
                                        viewModel.stopPlaying()
                                    } else {
                                        viewModel.startPlaying()
                                    }
                                },
                                startColor = Color(0xFFFF1744),
                                stopColor = Color(0xFFFDFDFF),
                                backgroundColor = Color(0xFF1A1A1A),
                                shadowColor = Color.Black.copy(alpha = 0.6f),
                                cornerRadius = 8.dp,
                                glowRadius = 3.dp
                            )


                        }
                        Spacer(Modifier.height(5.dp))
                        //line 0 : freq range
                        Row(modifier = Modifier.padding(top = 4.dp).align(Alignment.CenterHorizontally)) {
                            //### LED BARRE GAUCHE
                            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed4Active,
                                    color = Color(0xFF00E668),
                                    size = 20f,
                                    haloSpacer = 1 ,
                                    blinkInterval = 0)
                                Spacer(Modifier.height(0.dp))
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed3Active,
                                    color = Color(0xFF00E676),
                                    size = 18f,
                                    haloSpacer = 1 ,
                                    blinkInterval = 0)
                                Spacer(Modifier.height(0.dp))
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed2Active,
                                    color = Color(0xFF1DE9B6),
                                    size = 15f,
                                    haloSpacer = 1,
                                    blinkInterval = 0)
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed1Active,
                                    color = Color(0xFF00E5FF),
                                    size = 13f,
                                    haloSpacer = 1,
                                    blinkInterval = 0)
                            }
                            Spacer(Modifier.width(5.dp))
                            //#### [Base frequency settings]
                            Card(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(
                                    0xFF0A0A0A)
                                ),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp)
                            ) {
                                Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 0.dp), contentAlignment = Alignment.Center){
                                    Row(modifier = Modifier.align(Alignment.TopCenter).padding(top = 10.dp)) {
                                        //### knobs
                                        //max freq knob
                                        Column(modifier = Modifier.align(Alignment.CenterVertically).padding(top = 10.dp)) {

                                            RRKnobV2(
                                                size = 50.dp,
                                                steps = 7,
                                                onValueChanged = {delta ->
                                                    maxFreqValue += delta * 100.0
                                                },
                                                indicatorColor = Color(0xFFF50057),
                                                indicatorSecondaryColor = Color(0xFFFF1744),
                                                tickColor = Color(0xFF2F2D2D),
                                                activeTickColor = Color(0xFFFFFFFF),
                                                tickLength = 2.dp
                                            )
                                            Spacer(Modifier.height(10.dp))
                                            Text(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                text = "Max Hz",
                                                color = Color(0xFF312E2E),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Thin,
                                                fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                textAlign = TextAlign.Center,
                                                fontSize = 20.sp

                                            )

                                        }
                                        Spacer(Modifier.width(25.dp))
                                        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                            Text(text = "Freq Range",
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                color = Color(0xFF434344),
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                textAlign = TextAlign.Center,
                                                fontSize = 22.sp)
                                            Spacer(Modifier.height(8.dp))
                                            //### display freq range


                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFFFBFBFB),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.15f,
                                                        flickerFrequency = 1f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 5
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${maxFreqValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )
                                            Spacer(Modifier.height(10.dp))
                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 6.dp,
                                                        segmentHorizontalLength = 6.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFFF50057),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.15f,
                                                        flickerFrequency = 1f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 5
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${minFreqValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )

                                            Spacer(Modifier.height(25.dp))

                                        }
                                        Spacer(Modifier.width(25.dp))
                                        //min freq knob
                                        Column(modifier = Modifier.align(Alignment.CenterVertically).padding(top = 10.dp)) {
                                            RRKnobV2(
                                                size = 50.dp,
                                                steps = 7,
                                                onValueChanged = { delta ->
                                                    minFreqValue += delta * 100.0
                                                },
                                                indicatorColor = Color(0xFFF50057),
                                                indicatorSecondaryColor = Color(0xFFFF1744),
                                                tickColor = Color(0xFF2F2D2D),
                                                activeTickColor = Color(0xFFFFFFFF),
                                                tickLength = 2.dp
                                            )
                                            Spacer(Modifier.height(10.dp))
                                            Text(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                text = "Min Hz",
                                                color = Color(0xFF312E2E),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Thin,
                                                fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                textAlign = TextAlign.Center,
                                                fontSize = 20.sp
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.width(5.dp))
                            //#### LED BARRE DROITE
                            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed4Active,
                                    color = Color(0xFF00E668),
                                    size = 20f,
                                    haloSpacer = 1 ,
                                    blinkInterval = 0)
                                Spacer(Modifier.height(0.dp))
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed3Active,
                                    color = Color(0xFF00E676),
                                    size = 18f,
                                    haloSpacer = 1 ,
                                    blinkInterval = 0)
                                Spacer(Modifier.height(0.dp))
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed2Active,
                                    color = Color(0xFF1DE9B6),
                                    size = 15f,
                                    haloSpacer = 1,
                                    blinkInterval = 0)
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed1Active,
                                    color = Color(0xFF00E5FF),
                                    size = 13f,
                                    haloSpacer = 1,
                                    blinkInterval = 0)
                            }

                        }
                        Spacer(Modifier.height(10.dp))
                        //line 1 : amp mod + chaos
                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            //#### [Modulation settings]
                            Card(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF0C0C0C)),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp)
                            ) {
                                Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 0.dp), contentAlignment = Alignment.Center){
                                    Column(modifier = Modifier.align(Alignment.Center)) {
                                        Text(text = "Amp Mod",
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            color = Color(0xFF434344),
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily(Font(R.font.micro_regular)),
                                            textAlign = TextAlign.Center,
                                            fontSize = 22.sp)

                                        Spacer(Modifier.height(8.dp))
                                        //#### [Display AM values]
                                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            //modulation frequency knob
                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFF00E5FF),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.25f,
                                                        flickerFrequency = 1f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 3
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${ampModFrequencyValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                modifier = Modifier.align(Alignment.CenterVertically),
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )
                                            Spacer(Modifier.width(30.dp))
                                            //modulation Depth
                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFF2979FF),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.45f,
                                                        flickerFrequency = 1f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 3
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${ampModDepthValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                modifier = Modifier.align(Alignment.CenterVertically),
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )
                                        }
                                        Spacer(Modifier.height(20.dp))
                                        //#### [Knobs Amp mod ]
                                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            //modulation frequency knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {delta ->
                                                        ampModFrequencyValue += delta * 1.0},
                                                    indicatorColor = Color(0xFF651FFF),
                                                    indicatorSecondaryColor = Color(0xFF2979FF),
                                                    tickColor = Color(0xFF2F2D2D),
                                                    activeTickColor = Color(0xFF00E5FF),
                                                    tickLength = 2.dp
                                                )
                                                Spacer(Modifier.height(10.dp))
                                                Text(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    text = "Frequency",
                                                    color = Color(0xFF312E2E),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Thin,
                                                    fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 18.sp

                                                )

                                            }
                                            Spacer(Modifier.width(30.dp))
                                            //modulation Depth knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {delta ->
                                                        ampModDepthValue += delta * 1.0},
                                                    indicatorColor = Color(0xFF651FFF),
                                                    indicatorSecondaryColor = Color(0xFF2979FF),
                                                    tickColor = Color(0xFF2F2D2D),
                                                    activeTickColor = Color(0xFF00E5FF),
                                                    tickLength = 2.dp
                                                )
                                                Spacer(Modifier.height(10.dp))
                                                Text(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    text = "Depth",
                                                    color = Color(0xFF312E2E),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Thin,
                                                    fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 18.sp
                                                )
                                            }
                                        }
                                        Spacer(Modifier.height(3.dp))
                                    }
                                }
                            }
                            Spacer(Modifier.width(10.dp))
                            //#### [Chaos settings]
                            Card(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors( containerColor = Color(0xFF0C0C0C)),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp )
                            ) {
                                Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 0.dp), contentAlignment = Alignment.Center){
                                    Column(modifier = Modifier.align(Alignment.Center)) {
                                        Text(text = "Chaos",
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            color = Color(0xFF434344),
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily(Font(R.font.micro_regular)),
                                            textAlign = TextAlign.Center,
                                            fontSize = 22.sp)
                                        Spacer(Modifier.height(8.dp))
                                        //#### [Display chaos values]
                                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            //freq value
                                            DynamikRowAfficheur(
                                                modifier = Modifier.align(Alignment.CenterVertically),
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFFC4FD00),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.25f,
                                                        flickerFrequency = 1f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 3
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${chaosFrequencyValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )
                                            Spacer(Modifier.width(30.dp))
                                            // Depth
                                            DynamikRowAfficheur(
                                                modifier = Modifier.align(Alignment.CenterVertically),
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFF76FF03),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.35f,
                                                        flickerFrequency = 1f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 3
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${(chaosDepthValue).toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )
                                        }
                                        Spacer(Modifier.height(20.dp))
                                        //#### [knob chaos ]
                                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            //Chaos frequency knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {delta ->
                                                        chaosFrequencyValue += delta * 1.0
                                                    },
                                                    indicatorColor = Color(0xFF00E676),
                                                    indicatorSecondaryColor = Color(0xFF76FF03),
                                                    tickColor = Color(0xFF2F2D2D),
                                                    activeTickColor = Color(0xFFC6FF00),
                                                    tickLength = 2.dp
                                                )
                                                Spacer(Modifier.height(10.dp))
                                                Text(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    text = "Frequency",
                                                    color = Color(0xFF312E2E),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Thin,
                                                    fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 18.sp

                                                )

                                            }
                                            Spacer(Modifier.width(30.dp))
                                            //Chaos Depth knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {delta ->
                                                        chaosDepthValue += delta * 1.0
                                                    },
                                                    indicatorColor = Color(0xFF00E676),
                                                    indicatorSecondaryColor = Color(0xFF76FF03),
                                                    tickColor = Color(0xFF2F2D2D),
                                                    activeTickColor = Color(0xFFC6FF00),
                                                    tickLength = 2.dp
                                                )
                                                Spacer(Modifier.height(10.dp))
                                                Text(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    text = "Depth",
                                                    color = Color(0xFF312E2E),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Thin,
                                                    fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 18.sp
                                                )
                                            }
                                        }
                                        Spacer(Modifier.height(3.dp))
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))
                        //line 2 : burst + deviation
                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            //#### [burst settings]
                            Card(
                                modifier = Modifier.align(Alignment.Top),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF0C0C0C)),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp)
                            ) {
                                Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp), contentAlignment = Alignment.Center){
                                    Row(modifier = Modifier.align(Alignment.Center)) {
                                        //#### [knob burst ]
                                        Column( modifier = Modifier.align(Alignment.CenterVertically).padding(top = 8.dp,bottom = 8.dp)) {
                                            RRKnobV2(
                                                size = 50.dp,
                                                steps = 7,
                                                onValueChanged = {delta ->
                                                    burstProbabilityValue += (delta * 1.0).toInt()},
                                                indicatorColor = Color(0xFFFFC400),
                                                indicatorSecondaryColor = Color(0xFFFF9100),
                                                tickColor = Color(0xFF2F2D2D),
                                                activeTickColor = Color(0xFFFF3D00),
                                                tickLength = 2.dp
                                            )

                                        }
                                        Spacer(Modifier.width(30.dp))
                                        Column(modifier = Modifier.align(Alignment.Top).padding(top = 0.dp)) {
                                            Text(text = "% Burst",
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                color = Color(0xFF434344),
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                textAlign = TextAlign.Center,
                                                fontSize = 22.sp)
                                            Spacer(Modifier.height(8.dp))
                                            //#### [freq dev values]
                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFFFF3D00),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.25f,
                                                        flickerFrequency = 2f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 3
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${burstProbabilityValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )


                                        }
                                    }
                                }
                            }

                            Spacer(Modifier.width(10.dp))
                            //#### [Freq deviation settings]
                            Card(
                                modifier = Modifier.align(Alignment.Top),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF0C0C0C)),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp)
                            ) {
                                Box(modifier = Modifier.padding(start = 15.dp, end = 20.dp, top = 10.dp, bottom = 10.dp), contentAlignment = Alignment.Center){
                                    Row(modifier = Modifier.align(Alignment.Center)) {
                                        Column(modifier = Modifier.align(Alignment.Top).padding(top = 0.dp)) {
                                            Text(text = "Deviation",
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                color = Color(0xFF434344),
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                textAlign = TextAlign.Center,
                                                fontSize = 22.sp)
                                            Spacer(Modifier.height(8.dp))
                                            //#### [freq dev values]
                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFFAD64F6),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.25f,
                                                        flickerFrequency = 2f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 3
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${freqDevValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )


                                        }
                                        Spacer(Modifier.width(30.dp))
                                        //#### [knob deviation ]
                                        Column( modifier = Modifier.align(Alignment.CenterVertically).padding(top = 8.dp,bottom = 8.dp)) {
                                            RRKnobV2(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                size = 50.dp,
                                                steps = 7,
                                                onValueChanged = {delta ->
                                                    freqDevValue += delta * 50.0},
                                                indicatorColor = Color(0xFF651FFF),
                                                indicatorSecondaryColor = Color(0xFF6F0581),
                                                tickColor = Color(0xFF2F2D2D),
                                                activeTickColor = Color(0xFFAD64F6),
                                                tickLength = 2.dp
                                            )

                                        }
                                    }




                                }
                            }


                        }

                    }

                }
            }
        )
    }

}




@Composable
fun JammerUIButtonPreview() {
    var isEmitting by remember { mutableStateOf(false) }

    var isClassicJammerMode by remember { mutableStateOf(true) }

    var isVisible by remember { mutableStateOf(true) }

    //todo UI TEST ONLY - to replace by jamconfig from viewmodel
    var ampModFrequencyValue by remember { mutableStateOf(5.0) }
    var ampModDepthValue by remember { mutableStateOf(6.0) }// real val is divised by 10 (0.6)
    var chaosFrequencyValue by remember { mutableStateOf(130.0) }
    var chaosDepthValue by remember { mutableStateOf(150.0) }
    var burstProbabilityValue by remember { mutableStateOf(3f) }// real val is divised by 10 (0.3)
    var clipFactorValue by remember { mutableStateOf(12f) }// real val is divised by 10 (1.2)
    var minFreqValue by remember { mutableStateOf(17500.0) }
    var maxFreqValue by remember { mutableStateOf(18500.0) }
    var freqDevValue by remember { mutableStateOf(500.0) }


    // led activation
    var isLed1Active by remember { mutableStateOf(false) }
    var isLed2Active by remember { mutableStateOf(false) }
    var isLed3Active by remember { mutableStateOf(false) }
    var isLed4Active by remember { mutableStateOf(false) }


    LaunchedEffect(isEmitting) {
        while (isEmitting) {
            delay(100)
            isLed1Active = !isLed1Active
            delay(100)
            isLed2Active = !isLed2Active
            delay(100)
            isLed3Active = !isLed3Active
            delay(100)
            isLed4Active = !isLed4Active
            delay(100)
        }
        isLed1Active = false
        isLed2Active = false
        isLed3Active = false
        isLed4Active = false
    }


    //todo -------------------------------------------------------------

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A)) // temporaire pour debug visuel
    ) {


        // main visual
        RepeatingRippleCyberCanvas(
            modifier = Modifier.fillMaxSize()
                .clickable { isVisible = !isVisible },
            intervalMs = 1000,//1000
            speed = 0.99f,
            amplitude = 150f,
            gridSize = 45, //20 (lower = more dots so need good smartphone (galaxy S))
            isActive = isEmitting,
            colorBase = Color(0xFFF50057),//0xFFF50057
            colorAccent = Color(0xFFFF9100), // 0xFF1A181A
            radiusDots = 3.5f,
            frameRate = 42L
        )





        // dual modal for settings
        SyncedDualModalSheetGlassUp(
            visible = isVisible,
            onDismiss = { isVisible = false },
            scrimMaxAlpha = 0.2f,
            topSheetRatio = 0.40f,
            bottomSheetRatio = 0.60f,
            autoDismissThresholdRatio = 0.10f,
            modalBackgroundColor = Color(0xED050505),
            cornerRadius = 20.dp,
            peekHeight = 0.dp,
            topContent = {
                //top Modal : values
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(modifier = Modifier.padding(top = 10.dp).fillMaxWidth()) {
                        Image(
                            painter = painterResource(id = R.drawable.jamicon),
                            contentDescription = "logoapp",
                            modifier = Modifier.size(180.dp).align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop // ou Fit, FillBounds, etc.
                        )
                    }



                }
            },
            bottomContent = {
                //Bottom Modal : knobs
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Column(modifier = Modifier.padding(top = 0.dp).fillMaxWidth()) {

                        Spacer(Modifier.height(10.dp))

                        //buttons
                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {

                            RRStartStopButtonRect(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                width = 150.dp,
                                height = 40.dp,
                                isStarted = isClassicJammerMode,
                                onToggle = { isClassicJammerMode = !isClassicJammerMode },
                                startColor = Color(0xFFFFAB00),
                                stopColor = Color(0xFF00BFA5),
                                textA = "RAW",
                                textB = "SCO",
                                backgroundColor = Color(0xFF1A1A1A),
                                shadowColor = Color.Black.copy(alpha = 0.6f),
                                cornerRadius = 8.dp,
                                glowRadius = 3.dp
                            )
                            Spacer(Modifier.width(20.dp))

                            //Start/Stop button
                            RRStartStopButtonRect(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                width = 150.dp,
                                height = 40.dp,
                                isStarted = isEmitting,
                                onToggle = { isEmitting = !isEmitting },
                                startColor = Color(0xFFFF1744),
                                stopColor = Color(0xFFFDFDFF),
                                backgroundColor = Color(0xFF1A1A1A),
                                shadowColor = Color.Black.copy(alpha = 0.6f),
                                cornerRadius = 8.dp,
                                glowRadius = 3.dp
                            )


                        }
                        Spacer(Modifier.height(5.dp))
                        //line 0 : freq range
                        Row(modifier = Modifier.padding(top = 4.dp).align(Alignment.CenterHorizontally)) {
                            //### LED BARRE GAUCHE
                            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed4Active,
                                    color = Color(0xFF00E668),
                                    size = 20f,
                                    haloSpacer = 1 ,
                                    blinkInterval = 0)
                                Spacer(Modifier.height(0.dp))
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed3Active,
                                    color = Color(0xFF00E676),
                                    size = 18f,
                                    haloSpacer = 1 ,
                                    blinkInterval = 0)
                                Spacer(Modifier.height(0.dp))
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed2Active,
                                    color = Color(0xFF1DE9B6),
                                    size = 15f,
                                    haloSpacer = 1,
                                    blinkInterval = 0)
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed1Active,
                                    color = Color(0xFF00E5FF),
                                    size = 13f,
                                    haloSpacer = 1,
                                    blinkInterval = 0)
                            }
                            Spacer(Modifier.width(5.dp))
                            //#### [Base frequency settings]
                            Card(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(
                                    0xFF0A0A0A)
                                ),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp)
                            ) {
                                Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 0.dp), contentAlignment = Alignment.Center){
                                    Row(modifier = Modifier.align(Alignment.TopCenter).padding(top = 10.dp)) {
                                        //### knobs
                                        //max freq knob
                                        Column(modifier = Modifier.align(Alignment.CenterVertically).padding(top = 10.dp)) {

                                            RRKnobV2(
                                                size = 50.dp,
                                                steps = 7,
                                                onValueChanged = {delta ->
                                                    maxFreqValue += delta * 100.0
                                                },
                                                indicatorColor = Color(0xFFF50057),
                                                indicatorSecondaryColor = Color(0xFFFF1744),
                                                tickColor = Color(0xFF2F2D2D),
                                                activeTickColor = Color(0xFFFFFFFF),
                                                tickLength = 2.dp
                                            )
                                            Spacer(Modifier.height(10.dp))
                                            Text(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                text = "Max Hz",
                                                color = Color(0xFF312E2E),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Thin,
                                                fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                textAlign = TextAlign.Center,
                                                fontSize = 20.sp

                                            )

                                        }
                                        Spacer(Modifier.width(25.dp))
                                        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                            Text(text = "Freq Range",
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                color = Color(0xFF434344),
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                textAlign = TextAlign.Center,
                                                fontSize = 22.sp)
                                            Spacer(Modifier.height(8.dp))
                                            //### display freq range


                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFFFBFBFB),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.15f,
                                                        flickerFrequency = 1f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 5
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${maxFreqValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )
                                            Spacer(Modifier.height(10.dp))
                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 6.dp,
                                                        segmentHorizontalLength = 6.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFFF50057),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.15f,
                                                        flickerFrequency = 1f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 5
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${minFreqValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )

                                            Spacer(Modifier.height(25.dp))

                                        }
                                        Spacer(Modifier.width(25.dp))
                                        //min freq knob
                                        Column(modifier = Modifier.align(Alignment.CenterVertically).padding(top = 10.dp)) {
                                            RRKnobV2(
                                                size = 50.dp,
                                                steps = 7,
                                                onValueChanged = { delta ->
                                                    minFreqValue += delta * 100.0
                                                },
                                                indicatorColor = Color(0xFFF50057),
                                                indicatorSecondaryColor = Color(0xFFFF1744),
                                                tickColor = Color(0xFF2F2D2D),
                                                activeTickColor = Color(0xFFFFFFFF),
                                                tickLength = 2.dp
                                            )
                                            Spacer(Modifier.height(10.dp))
                                            Text(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                text = "Min Hz",
                                                color = Color(0xFF312E2E),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Thin,
                                                fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                textAlign = TextAlign.Center,
                                                fontSize = 20.sp
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.width(5.dp))
                            //#### LED BARRE DROITE
                            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed4Active,
                                    color = Color(0xFF00E668),
                                    size = 20f,
                                    haloSpacer = 1 ,
                                    blinkInterval = 0)
                                Spacer(Modifier.height(0.dp))
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed3Active,
                                    color = Color(0xFF00E676),
                                    size = 18f,
                                    haloSpacer = 1 ,
                                    blinkInterval = 0)
                                Spacer(Modifier.height(0.dp))
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed2Active,
                                    color = Color(0xFF1DE9B6),
                                    size = 15f,
                                    haloSpacer = 1,
                                    blinkInterval = 0)
                                RealisticLED(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    isOn = isLed1Active,
                                    color = Color(0xFF00E5FF),
                                    size = 13f,
                                    haloSpacer = 1,
                                    blinkInterval = 0)
                            }

                        }
                        Spacer(Modifier.height(10.dp))
                        //line 1 : amp mod + chaos
                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            //#### [Modulation settings]
                            Card(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF0C0C0C)),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp)
                            ) {
                                Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 0.dp), contentAlignment = Alignment.Center){
                                    Column(modifier = Modifier.align(Alignment.Center)) {
                                        Text(text = "Amp Mod",
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            color = Color(0xFF434344),
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily(Font(R.font.micro_regular)),
                                            textAlign = TextAlign.Center,
                                            fontSize = 22.sp)

                                        Spacer(Modifier.height(8.dp))
                                        //#### [Display AM values]
                                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            //modulation frequency knob
                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFF00E5FF),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.25f,
                                                        flickerFrequency = 1f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 3
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${ampModFrequencyValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                modifier = Modifier.align(Alignment.CenterVertically),
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )
                                            Spacer(Modifier.width(30.dp))
                                            //modulation Depth
                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFF2979FF),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.45f,
                                                        flickerFrequency = 1f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 3
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${ampModDepthValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                modifier = Modifier.align(Alignment.CenterVertically),
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )
                                        }
                                        Spacer(Modifier.height(20.dp))
                                        //#### [Knobs Amp mod ]
                                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            //modulation frequency knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {delta ->
                                                        ampModFrequencyValue += delta * 1.0},
                                                    indicatorColor = Color(0xFF651FFF),
                                                    indicatorSecondaryColor = Color(0xFF2979FF),
                                                    tickColor = Color(0xFF2F2D2D),
                                                    activeTickColor = Color(0xFF00E5FF),
                                                    tickLength = 2.dp
                                                )
                                                Spacer(Modifier.height(10.dp))
                                                Text(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    text = "Frequency",
                                                    color = Color(0xFF312E2E),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Thin,
                                                    fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 18.sp

                                                )

                                            }
                                            Spacer(Modifier.width(30.dp))
                                            //modulation Depth knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {delta ->
                                                        ampModDepthValue += delta * 1.0},
                                                    indicatorColor = Color(0xFF651FFF),
                                                    indicatorSecondaryColor = Color(0xFF2979FF),
                                                    tickColor = Color(0xFF2F2D2D),
                                                    activeTickColor = Color(0xFF00E5FF),
                                                    tickLength = 2.dp
                                                )
                                                Spacer(Modifier.height(10.dp))
                                                Text(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    text = "Depth",
                                                    color = Color(0xFF312E2E),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Thin,
                                                    fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 18.sp
                                                )
                                            }
                                        }
                                        Spacer(Modifier.height(3.dp))
                                    }
                                }
                            }
                            Spacer(Modifier.width(10.dp))
                            //#### [Chaos settings]
                            Card(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors( containerColor = Color(0xFF0C0C0C)),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp )
                            ) {
                                Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 0.dp), contentAlignment = Alignment.Center){
                                    Column(modifier = Modifier.align(Alignment.Center)) {
                                        Text(text = "Chaos",
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            color = Color(0xFF434344),
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily(Font(R.font.micro_regular)),
                                            textAlign = TextAlign.Center,
                                            fontSize = 22.sp)
                                        Spacer(Modifier.height(8.dp))
                                        //#### [Display chaos values]
                                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            //freq value
                                            DynamikRowAfficheur(
                                                modifier = Modifier.align(Alignment.CenterVertically),
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFFC4FD00),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.25f,
                                                        flickerFrequency = 1f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 3
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${chaosFrequencyValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )
                                            Spacer(Modifier.width(30.dp))
                                            // Depth
                                            DynamikRowAfficheur(
                                                modifier = Modifier.align(Alignment.CenterVertically),
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFF76FF03),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.35f,
                                                        flickerFrequency = 1f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 3
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${(chaosDepthValue).toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )
                                        }
                                        Spacer(Modifier.height(20.dp))
                                        //#### [knob chaos ]
                                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            //Chaos frequency knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {delta ->
                                                        chaosFrequencyValue += delta * 1.0
                                                    },
                                                    indicatorColor = Color(0xFF00E676),
                                                    indicatorSecondaryColor = Color(0xFF76FF03),
                                                    tickColor = Color(0xFF2F2D2D),
                                                    activeTickColor = Color(0xFFC6FF00),
                                                    tickLength = 2.dp
                                                )
                                                Spacer(Modifier.height(10.dp))
                                                Text(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    text = "Frequency",
                                                    color = Color(0xFF312E2E),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Thin,
                                                    fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 18.sp

                                                )

                                            }
                                            Spacer(Modifier.width(30.dp))
                                            //Chaos Depth knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {delta ->
                                                        chaosDepthValue += delta * 1.0
                                                    },
                                                    indicatorColor = Color(0xFF00E676),
                                                    indicatorSecondaryColor = Color(0xFF76FF03),
                                                    tickColor = Color(0xFF2F2D2D),
                                                    activeTickColor = Color(0xFFC6FF00),
                                                    tickLength = 2.dp
                                                )
                                                Spacer(Modifier.height(10.dp))
                                                Text(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    text = "Depth",
                                                    color = Color(0xFF312E2E),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Thin,
                                                    fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 18.sp
                                                )
                                            }
                                        }
                                        Spacer(Modifier.height(3.dp))
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))
                        //line 2 : burst + deviation
                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            //#### [burst settings]
                            Card(
                                modifier = Modifier.align(Alignment.Top),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF0C0C0C)),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp)
                            ) {
                                Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp), contentAlignment = Alignment.Center){
                                    Row(modifier = Modifier.align(Alignment.Center)) {
                                        //#### [knob burst ]
                                        Column( modifier = Modifier.align(Alignment.CenterVertically).padding(top = 8.dp,bottom = 8.dp)) {
                                            RRKnobV2(
                                                size = 50.dp,
                                                steps = 7,
                                                onValueChanged = {delta ->
                                                    burstProbabilityValue += (delta * 1.0).toInt()},
                                                indicatorColor = Color(0xFFFFC400),
                                                indicatorSecondaryColor = Color(0xFFFF9100),
                                                tickColor = Color(0xFF2F2D2D),
                                                activeTickColor = Color(0xFFFF3D00),
                                                tickLength = 2.dp
                                            )

                                        }
                                        Spacer(Modifier.width(30.dp))
                                        Column(modifier = Modifier.align(Alignment.Top).padding(top = 0.dp)) {
                                            Text(text = "% Burst",
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                color = Color(0xFF434344),
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                textAlign = TextAlign.Center,
                                                fontSize = 22.sp)
                                            Spacer(Modifier.height(8.dp))
                                            //#### [freq dev values]
                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFFFF3D00),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.25f,
                                                        flickerFrequency = 2f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 3
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${burstProbabilityValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )


                                        }
                                    }
                                }
                            }

                            Spacer(Modifier.width(10.dp))
                            //#### [Freq deviation settings]
                            Card(
                                modifier = Modifier.align(Alignment.Top),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF0C0C0C)),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp)
                            ) {
                                Box(modifier = Modifier.padding(start = 15.dp, end = 20.dp, top = 10.dp, bottom = 10.dp), contentAlignment = Alignment.Center){
                                    Row(modifier = Modifier.align(Alignment.Center)) {
                                        Column(modifier = Modifier.align(Alignment.Top).padding(top = 0.dp)) {
                                            Text(text = "Deviation",
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                color = Color(0xFF434344),
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.micro_regular)),
                                                textAlign = TextAlign.Center,
                                                fontSize = 22.sp)
                                            Spacer(Modifier.height(8.dp))
                                            //#### [freq dev values]
                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 8.dp,
                                                        segmentHorizontalLength = 8.dp,
                                                        segmentThickness = 2.dp,
                                                        bevel = 1.dp,
                                                        onColor = Color(0xFFAD64F6),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.25f,
                                                        flickerFrequency = 2f,
                                                        idleMode = false,
                                                        idleSpeed = 100
                                                    ),
                                                    nbrDigit = 3
                                                ),
                                                reflectConfig = reflectConfig(),
                                                overrideValue = "${freqDevValue.toInt()}",
                                                reversedOverride = true,
                                                showZeroWhenEmpty = true,
                                                activateReflect = false,
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                extraSpacingStep = 0,
                                                extraSpacing = 15.dp
                                            )


                                        }
                                        Spacer(Modifier.width(30.dp))
                                        //#### [knob deviation ]
                                        Column( modifier = Modifier.align(Alignment.CenterVertically).padding(top = 8.dp,bottom = 8.dp)) {
                                            RRKnobV2(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                size = 50.dp,
                                                steps = 7,
                                                onValueChanged = {delta ->
                                                    freqDevValue += delta * 50.0},
                                                indicatorColor = Color(0xFF651FFF),
                                                indicatorSecondaryColor = Color(0xFF6F0581),
                                                tickColor = Color(0xFF2F2D2D),
                                                activeTickColor = Color(0xFFAD64F6),
                                                tickLength = 2.dp
                                            )

                                        }
                                    }




                                }
                            }


                        }

                    }

                }
            }
        )
    }
}