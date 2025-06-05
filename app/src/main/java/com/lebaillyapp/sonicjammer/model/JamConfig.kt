package com.lebaillyapp.sonicjammer.model

/**
 * ### JamConfig
 *
 * Configuration data class for the ultrasonic signal generator.
 *
 * @property sampleRate The audio sampling rate in Hertz (e.g., 44100).
 * @property baseFreq The central carrier frequency in Hertz (e.g., 17800).
 * @property freqDev The frequency deviation amplitude for FM modulation in Hertz (e.g., 500).
 * @property modFreq The frequency of AM and FM modulation in Hertz (e.g., 5).
 * @property modDepth The AM modulation depth (range 0 to 1, e.g., 0.6).
 * @property clipFactor Clipping factor applied to the signal (e.g., 1.2).
 * @property burstProbability Probability of a random silent burst occurring (e.g., 0.3).
 * @property chaosFreq1 Frequency of secondary FM chaos modulation in Hertz (e.g., 130).
 * @property chaosDepth Depth of secondary FM chaos modulation in Hertz (e.g., 150).
 * @property bufferSize The audio buffer size in number of samples (e.g., 1024).
 */
data class JamConfig(
    val sampleRate: Int = 44100,
    val baseFreq: Double = 17800.0,
    val freqDev: Double = 500.0,
    val modFreq: Double = 5.0,
    val modDepth: Double = 0.6,
    val clipFactor: Double = 1.2,
    val burstProbability: Float = 0.3f,
    val chaosFreq1: Double = 130.0,
    val chaosDepth: Double = 150.0,
    val bufferSize: Int = 1024
)

