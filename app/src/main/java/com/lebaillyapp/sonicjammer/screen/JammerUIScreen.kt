package com.lebaillyapp.sonicjammer.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lebaillyapp.sonicjammer.composition.ButtonRockerSwitchVertical
import com.lebaillyapp.sonicjammer.viewmodel.JamViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lebaillyapp.sonicjammer.R
import com.lebaillyapp.sonicjammer.composition.RepeatingRippleCyberCanvas
import com.lebaillyapp.sonicjammer.composition.SyncedDualModalSheet
import com.lebaillyapp.sonicjammer.oldStuff.composable.afficheurs.DynamikRowAfficheur
import com.lebaillyapp.sonicjammer.oldStuff.composable.knob.RRKnobV2
import com.lebaillyapp.sonicjammer.oldStuff.config.SevenSegmentConfig
import com.lebaillyapp.sonicjammer.oldStuff.config.reflectConfig
import com.lebaillyapp.sonicjammer.oldStuff.iteratorConfigGenerator


@Composable
fun JammerUIScreen(
    viewModel: JamViewModel = viewModel()
) {
    val isEmitting by viewModel.isPlaying.collectAsState()

    // UI principale
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ButtonRockerSwitchVertical(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 52.dp),
            width = 55.dp,
            height = 110.dp,
            cornerRadius = 15.dp,
            isChecked = isEmitting,
            onToggle = { shouldStart ->
                if (shouldStart) {
                    viewModel.startPlaying()
                } else {
                    viewModel.stopPlaying()
                }
            },
            iconSource = painterResource(R.drawable.emit) // Assure-toi que cette ressource existe
        )


        RepeatingRippleCyberCanvas(
            modifier = Modifier.fillMaxSize(),
            intervalMs = 1000,//1000
            speed = 0.99f,
            amplitude = 150f,//150
            gridSize = 30, //20
            isActive = isEmitting,
            colorBase = Color(0xFFF50057),
            colorAccent = Color(0xFF1A181A), // couleur de la vague
            radiusDots = 3.5f,
            frameRate = 32L
        )



    }
}




@Composable
fun JammerUIButtonPreview() {
    var isChecked by remember { mutableStateOf(false) }

    var isVisible by remember { mutableStateOf(false) }

    //todo UI TEST ONLY - to replace by jamconfig from viewmodel
    var ampModFrequencyValue by remember { mutableStateOf(5.0) }
    var ampModDepthValue by remember { mutableStateOf(0.6) }
    var chaosFrequencyValue by remember { mutableStateOf(130.0) }
    var chaosDepthValue by remember { mutableStateOf(150.0) }
    var burstProbabilityValue by remember { mutableStateOf(0.3f) }
    var clipFactorValue by remember { mutableStateOf(1.2) }
    var minFreqValue by remember { mutableStateOf(17500.0) }
    var maxFreqValue by remember { mutableStateOf(18500.0) }


    var minFreqlastStep by remember { mutableStateOf(0) }


    //todo -------------------------------------------------------------

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A)) // temporaire pour debug visuel
    ) {


        // main visual
        RepeatingRippleCyberCanvas(
            modifier = Modifier.fillMaxSize(),
            intervalMs = 1000,//1000
            speed = 0.99f,
            amplitude = 150f,//150
            gridSize = 30, //20 (lower = more dots so need good smartphone (galaxy S))
            isActive = isChecked,
            colorBase = Color(0xFFF50057),//0xFFF50057
            colorAccent = Color(0xFFFF9100), // 0xFF1A181A
            radiusDots = 3.5f,
            frameRate = 32L
        )


        //button to replace
        Row(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 52.dp), horizontalArrangement = Arrangement.Center) {
            ButtonRockerSwitchVertical(
                modifier = Modifier.align(Alignment.CenterVertically),
                isChecked = isVisible ,
                onToggle = { isVisible  = !isVisible },
                width = 45.dp,
                height = 90.dp,
                cornerRadius = 25.dp,
                animationDuration = 200,
                iconSource = painterResource(R.drawable.menu_indicator)
            )
            Spacer(Modifier.width(16.dp))
            ButtonRockerSwitchVertical(
                modifier = Modifier.align(Alignment.CenterVertically),
                isChecked = isChecked,
                onToggle = { isChecked = it },
                width = 45.dp,
                height = 90.dp,
                cornerRadius = 25.dp,
                animationDuration = 200,
                iconSource = painterResource(R.drawable.emit)
            )
        }


        // dual modal for settings
        SyncedDualModalSheet(
            visible = isVisible,
            onDismiss = { isVisible = false },
            scrimMaxAlpha = 0.5f,
            topSheetRatio = 0.42f,
            bottomSheetRatio = 0.50f,
            autoDismissThresholdRatio = 0.10f,
            modalBackgroundColor = Color(0xEB131313),
            cornerRadius = 20.dp,
            peekHeight = 0.dp,
            topContent = {
                //top Modal : values
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

                    //Display Amp mod values
                    Row(modifier = Modifier.padding(top = 10.dp).fillMaxWidth()) {
                        Spacer(Modifier.width(10.dp))

                        //COL 1 ---
                        Column(modifier = Modifier.align(Alignment.CenterVertically)){



                            Spacer(Modifier.height(10.dp))
                            //#### [Display decoy values]
                            Card(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF0C0C0C)),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp)
                            ) {
                                Box(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 0.dp), contentAlignment = Alignment.Center){
                                    Column(modifier = Modifier.align(Alignment.Center)) {
                                        Text(text = "Decoy",
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            color = Color(0xFF434344),
                                            style = MaterialTheme.typography.labelLarge,
                                            textAlign = TextAlign.Center,
                                            fontSize = 12.sp)

                                        Spacer(Modifier.height(10.dp))
                                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            //%burst
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                                DynamikRowAfficheur(
                                                    configs = iteratorConfigGenerator(
                                                        sevenSegCfg = SevenSegmentConfig(
                                                            digit = null,
                                                            char = null,
                                                            manualSegments = null,
                                                            segmentLength = 12.dp,
                                                            segmentHorizontalLength = 12.dp,
                                                            segmentThickness = 3.dp,
                                                            bevel = 2.dp,
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
                                                    overrideValue = "${(burstProbabilityValue*10f).toInt()}",
                                                    reversedOverride = true,
                                                    showZeroWhenEmpty = true,
                                                    activateReflect = false,
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    extraSpacingStep = 0,
                                                    extraSpacing = 15.dp
                                                )
                                                Spacer(Modifier.height(10.dp))
                                                Text(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    text = "Burst(%)",
                                                    color = Color(0xFF312E2E),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 10.sp

                                                )

                                            }
                                            Spacer(Modifier.width(15.dp))
                                            //clip
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                DynamikRowAfficheur(
                                                    configs = iteratorConfigGenerator(
                                                        sevenSegCfg = SevenSegmentConfig(
                                                            digit = null,
                                                            char = null,
                                                            manualSegments = null,
                                                            segmentLength = 12.dp,
                                                            segmentHorizontalLength = 12.dp,
                                                            segmentThickness = 3.dp,
                                                            bevel = 2.dp,
                                                            onColor = Color(0xFFFFC400),
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
                                                    overrideValue = "${(clipFactorValue*10.0f).toInt()}",
                                                    reversedOverride = true,
                                                    showZeroWhenEmpty = true,
                                                    activateReflect = false,
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    extraSpacingStep = 0,
                                                    extraSpacing = 15.dp
                                                )
                                                Spacer(Modifier.height(10.dp))
                                                Text(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    text = "Clip",
                                                    color = Color(0xFF312E2E),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(10.dp))


                        }
                        Spacer(Modifier.width(10.dp))
                        //COL 2 ---
                        Column(modifier = Modifier.align(Alignment.CenterVertically)){
                            //#### [Display freq range]
                            Card(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors( containerColor = Color(0xFF0C0C0C)),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp )
                            ) {
                                Box(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 0.dp), contentAlignment = Alignment.Center){
                                    Column(modifier = Modifier.align(Alignment.Center)) {
                                        Text(text = "Freq Range",
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            color = Color(0xFF434344),
                                            style = MaterialTheme.typography.labelLarge,
                                            textAlign = TextAlign.Center,
                                            fontSize = 12.sp)

                                        Spacer(Modifier.height(10.dp))
                                        //min value
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 15.dp,
                                                        segmentHorizontalLength = 15.dp,
                                                        segmentThickness = 3.dp,
                                                        bevel = 2.dp,
                                                        onColor = Color(0xFFFBFBFB),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.25f,
                                                        flickerFrequency = 2f,
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
                                            Spacer(Modifier.height(10.dp))
                                            Text(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                text = "Min(Hz)",
                                                color = Color(0xFF312E2E),
                                                style = MaterialTheme.typography.labelSmall,
                                                textAlign = TextAlign.Center,
                                                fontSize = 10.sp

                                            )

                                        }
                                        Spacer(Modifier.height(10.dp))
                                        //max value
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                            DynamikRowAfficheur(
                                                configs = iteratorConfigGenerator(
                                                    sevenSegCfg = SevenSegmentConfig(
                                                        digit = null,
                                                        char = null,
                                                        manualSegments = null,
                                                        segmentLength = 15.dp,
                                                        segmentHorizontalLength = 15.dp,
                                                        segmentThickness = 3.dp,
                                                        bevel = 2.dp,
                                                        onColor = Color(0xFFFBFBFB),
                                                        offColor = Color(0xFF141617),
                                                        alpha = 1f,
                                                        glowRadius = 20f,
                                                        flickerAmplitude = 0.25f,
                                                        flickerFrequency = 2f,
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
                                            Text(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                text = "Max(Hz)",
                                                color = Color(0xFF312E2E),
                                                style = MaterialTheme.typography.labelSmall,
                                                textAlign = TextAlign.Center,
                                                fontSize = 10.sp

                                            )

                                        }
                                    }
                                }
                            }
                        }

                    }

                }
            },
            bottomContent = {
                //Bottom Modal : knobs
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Column(modifier = Modifier.padding(top = 10.dp).fillMaxWidth()) {
                        Spacer(Modifier.width(10.dp))
                        //line 1
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
                                            textAlign = TextAlign.Center,
                                            fontSize = 12.sp)

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
                                                overrideValue = "${(ampModDepthValue*10.0f).toInt()}",
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
                                                    onValueChanged = {},
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
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 10.sp

                                                )

                                            }
                                            Spacer(Modifier.width(30.dp))
                                            //modulation Depth knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {},
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
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 10.sp
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
                                            textAlign = TextAlign.Center,
                                            fontSize = 12.sp)
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
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 10.sp

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
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                        Spacer(Modifier.height(3.dp))
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        //line 2
                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {

                            //#### [Decoy settings]
                            Card(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF0C0C0C)),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp)
                            ) {
                                Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 0.dp), contentAlignment = Alignment.Center){
                                    Column(modifier = Modifier.align(Alignment.Center)) {
                                        Text(text = "Decoy",
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            color = Color(0xFF434344),
                                            style = MaterialTheme.typography.labelLarge,
                                            textAlign = TextAlign.Center,
                                            fontSize = 12.sp)

                                        Spacer(Modifier.height(10.dp))
                                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            //burstProbability knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {delta ->
                                                        burstProbabilityValue += (delta * 0.1f).toInt()},
                                                    indicatorColor = Color(0xFFFFC400),
                                                    indicatorSecondaryColor = Color(0xFFFF9100),
                                                    tickColor = Color(0xFF2F2D2D),
                                                    activeTickColor = Color(0xFFFF3D00),
                                                    tickLength = 2.dp
                                                )
                                                Spacer(Modifier.height(10.dp))
                                                Text(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    text = "% Burst",
                                                    color = Color(0xFF312E2E),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 10.sp

                                                )

                                            }
                                            Spacer(Modifier.width(35.dp))
                                            //clip factor knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {delta ->
                                                        clipFactorValue += delta * 1.0
                                                    },
                                                    indicatorColor = Color(0xFFFFC400),
                                                    indicatorSecondaryColor = Color(0xFFFF9100),
                                                    tickColor = Color(0xFF2F2D2D),
                                                    activeTickColor = Color(0xFFFF3D00),
                                                    tickLength = 2.dp
                                                )
                                                Spacer(Modifier.height(10.dp))
                                                Text(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    text = "Clip",
                                                    color = Color(0xFF312E2E),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.width(10.dp))
                            //#### [Base frequency settings]
                            Card(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                shape = RoundedCornerShape(10.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF0C0C0C)),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 10.dp)
                            ) {
                                Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 0.dp), contentAlignment = Alignment.Center){
                                    Column(modifier = Modifier.align(Alignment.Center)) {
                                        Text(text = "Freq Range",
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            color = Color(0xFF434344),
                                            style = MaterialTheme.typography.labelLarge,
                                            textAlign = TextAlign.Center,
                                            fontSize = 12.sp)

                                        Spacer(Modifier.height(10.dp))
                                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            //max freq knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {delta ->
                                                        maxFreqValue += delta * 10.0
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
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 10.sp

                                                )

                                            }
                                            Spacer(Modifier.width(35.dp))
                                            //min freq knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = { delta ->
                                                        minFreqValue += delta * 10.0
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
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 10.sp
                                                )
                                            }
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