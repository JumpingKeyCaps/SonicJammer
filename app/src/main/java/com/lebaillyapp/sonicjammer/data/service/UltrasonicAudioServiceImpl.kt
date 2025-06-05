package com.lebaillyapp.sonicjammer.data.service

import com.lebaillyapp.sonicjammer.model.JamConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random


/**
 * ### UltrasonicAudioServiceImpl
 *
 * Concrete implementation of [UltrasonicAudioService] that generates a modulated ultrasonic signal
 * intended to jam audio/video recordings.
 *
 * The implementation respects the configuration provided via [updateConfig].
 */
class UltrasonicAudioServiceImpl : UltrasonicAudioService {

    private var config: JamConfig = JamConfig()

    /**
     * #### updateConfig
     *
     * Updates the configuration used for signal generation.
     *
     * @param newConfig The new configuration to apply for signal generation.
     */
    override fun updateConfig(newConfig: JamConfig) {
        config = newConfig
    }

    /**
     * #### generateSignalFlow
     *
     * Continuously generates and emits audio buffers according to the current configuration.
     *
     * Each buffer contains samples modulated to produce the ultrasonic jamming signal.
     *
     * @return A [Flow] emitting [ShortArray] audio buffers to be played or processed downstream.
     */
    override fun generateSignalFlow(): Flow<ShortArray> = flow {
        val bufferSize = config.bufferSize
        val sampleRate = config.sampleRate

        var sampleIndex = 0L

        while (true) {
            // Step 1: Generate buffer in Double precision (normalized [-1.0, 1.0])
            val doubleBuffer = DoubleArray(bufferSize) { i ->
                val time = (sampleIndex + i).toDouble() / sampleRate

                val fmMod = config.chaosDepth * sin(2 * PI * config.chaosFreq1 * time)
                val instFreq = config.baseFreq +
                        config.freqDev * sin(2 * PI * config.modFreq * time) +
                        fmMod
                val carrier = sin(2 * PI * instFreq * time)
                val amplitudeMod = 1.0 + config.modDepth * sin(2 * PI * config.modFreq * time)

                var sample = carrier * amplitudeMod
                sample = (sample * config.clipFactor).coerceIn(-1.0, 1.0)

                // Random bursts of silence to break signal regularity
                if (Random.nextFloat() < config.burstProbability) 0.0 else sample
            }

            sampleIndex += bufferSize

            // Step 2: Convert normalized doubles [-1.0, 1.0] to Shorts [-32768, 32767]
            val shortBuffer = ShortArray(bufferSize) { i ->
                (doubleBuffer[i] * Short.MAX_VALUE).toInt().toShort()
            }

            emit(shortBuffer)
        }
    }
}