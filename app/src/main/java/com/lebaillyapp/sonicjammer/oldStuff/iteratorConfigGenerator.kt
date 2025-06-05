package com.lebaillyapp.sonicjammer.oldStuff

import com.lebaillyapp.sonicjammer.oldStuff.config.SevenSegmentConfig


fun iteratorConfigGenerator(sevenSegCfg: SevenSegmentConfig,nbrDigit: Int): List<SevenSegmentConfig>{
    val list = mutableListOf<SevenSegmentConfig>()
    for (i in 1..nbrDigit){
        list.add(sevenSegCfg)
    }
    return list
}