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
import com.lebaillyapp.sonicjammer.oldStuff.composable.knob.RRKnobV2


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

    //  Réduis la taille du canvas pour test
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A)) // temporaire pour debug visuel
    ) {



        RepeatingRippleCyberCanvas(
            modifier = Modifier.fillMaxSize(),
            intervalMs = 1000,//1000
            speed = 0.99f,
            amplitude = 150f,//150
            gridSize = 30, //20
            isActive = isChecked,
            colorBase = Color(0xFFF50057),
            colorAccent = Color(0xFF1A181A), // couleur de la vague
            radiusDots = 3.5f,
            frameRate = 32L
        )



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



        SyncedDualModalSheet(
            visible = isVisible,
            onDismiss = { isVisible = false },
            scrimMaxAlpha = 0.5f,
            topSheetRatio = 0.40f,
            bottomSheetRatio = 0.35f,
            autoDismissThresholdRatio = 0.10f,
            modalBackgroundColor = Color(0xAD131313),
            cornerRadius = 20.dp,
            peekHeight = 0.dp,
            topContent = {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    }
                }
            },
            bottomContent = {
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

                                        Spacer(Modifier.height(10.dp))
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
                                            Spacer(Modifier.width(35.dp))
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
                                        Spacer(Modifier.height(10.dp))
                                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            //Chaos frequency knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {},
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
                                            Spacer(Modifier.width(35.dp))
                                            //Chaos Depth knob
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                RRKnobV2(
                                                    size = 50.dp,
                                                    steps = 7,
                                                    onValueChanged = {},
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
                                                    onValueChanged = {},
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
                                                    onValueChanged = {},
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
                                                    onValueChanged = {},
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
                                                    onValueChanged = {},
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