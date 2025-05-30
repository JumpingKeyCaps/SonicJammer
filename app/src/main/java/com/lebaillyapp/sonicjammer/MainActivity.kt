package com.lebaillyapp.sonicjammer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lebaillyapp.sonicjammer.config.SevenSegmentConfig
import com.lebaillyapp.sonicjammer.screen.DemoFive
import com.lebaillyapp.sonicjammer.screen.DemoFour
import com.lebaillyapp.sonicjammer.screen.DemoOne
import com.lebaillyapp.sonicjammer.screen.DemoSix
import com.lebaillyapp.sonicjammer.screen.DemoThree
import com.lebaillyapp.sonicjammer.screen.DemoTwo
import com.lebaillyapp.sonicjammer.ui.theme.SonicJammerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SonicJammerTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {

               //     DemoOne()
                 //   DemoTwo()
                   // DemoThree()

                   // DemoFour()
                    //DemoFive()
                    DemoSix()


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
