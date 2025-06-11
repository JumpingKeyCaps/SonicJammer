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
     * The central frequency for each buffer is randomly selected within the configured range.
     *
     * @return A [Flow] emitting [ShortArray] audio buffers to be played or processed downstream.
     */
     fun generateSignalFlow_OG(): Flow<ShortArray> = flow {
        val bufferSize = config.bufferSize
        val sampleRate = config.sampleRate
        val minFreq = config.minFreq
        val maxFreq = config.maxFreq

        var sampleIndex = 0L
        var phase = 0.0 // phase cumulée pour la FM
        val twoPi = 2 * PI

        while (true) {
            // Détermine la fréquence de base pour ce buffer
            val currentBaseFreq = if (minFreq == maxFreq) minFreq else Random.nextDouble(minFreq, maxFreq)

            // Génère le signal FM (sans AM)
            val doubleBuffer = DoubleArray(bufferSize) { i ->
                val t = (sampleIndex + i).toDouble() / sampleRate

                // Fréquence instantanée avec chaos éventuel
                val fmMod = config.chaosDepth * sin(twoPi * config.chaosFreq1 * t)
                val instFreq = (currentBaseFreq +
                        config.freqDev * sin(twoPi * config.modFreq * t) +
                        fmMod
                        ).coerceIn(minFreq, maxFreq)

                // Phase intégrée
                phase += twoPi * instFreq / sampleRate
                if (phase > twoPi) phase -= twoPi

                val sample = sin(phase)

                // Bursts de silence aléatoires
                if (Random.nextFloat() < config.burstProbability) 0.0 else sample
            }

            sampleIndex += bufferSize

            // Conversion en Short [-32768, 32767]
            val shortBuffer = ShortArray(bufferSize) { i ->
                (doubleBuffer[i].coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
            }



            emit(shortBuffer)
        }
    }

     fun generateSignalFlowO_G2(): Flow<ShortArray> = flow {
        val bufferSize = config.bufferSize
        val sampleRate = config.sampleRate
        val minFreq = config.minFreq
        val maxFreq = config.maxFreq

        var sampleIndex = 0L
        var phase = 0.0  // phase cumulée pour la FM test

        while (true) {
            // Step 0: Determine the current base frequency for this buffer
            // It's randomly chosen within the configured range for each new buffer.
            val currentBaseFreq = if (minFreq == maxFreq) minFreq else Random.nextDouble(minFreq, maxFreq)



            //todo mono frequence test -----------------
            // On génère le buffer avec modulation FM autour de baseFreq
            val doubleBuffer = DoubleArray(bufferSize) { i ->
                val t = (sampleIndex + i).toDouble() / sampleRate
                val instFreq = currentBaseFreq + config.freqDev * sin(2 * PI * config.modFreq * t)
                phase += 2 * PI * instFreq / sampleRate
                if (phase > 2 * PI) phase -= 2 * PI
                sin(phase)
            }
            //todo -------------------------

            sampleIndex += bufferSize

            // Step 2: Convert normalized doubles [-1.0, 1.0] to Shorts [-32768, 32767]
            val shortBuffer = ShortArray(bufferSize) { i ->
                (doubleBuffer[i] * Short.MAX_VALUE).toInt().toShort()
            }

            emit(shortBuffer)
        }
    }


    override fun generateSignalFlow(): Flow<ShortArray> = flow {
        val bufferSize = config.bufferSize
        val sampleRate = config.sampleRate
        val minFreq = config.minFreq
        val maxFreq = config.maxFreq
        val freqDev = config.freqDev
        // config.modFreq ne sera plus la valeur fixe utilisée pour la modulation, mais la plage est tirée de votre besoin (1 à 8 Hz)

        var sampleIndex = 0L
        var phaseMinFreq = 0.0
        var phaseMaxFreq = 0.0

        val random = Random(System.currentTimeMillis()) // Initialise un générateur de nombres aléatoires

        while (true) {
            // --- Générer de nouvelles fréquences de modulation aléatoires pour ce buffer ---
            // Chaque courbe aura sa propre modFreq aléatoire entre 1.0 et 8.0 Hz
            val randomModFreqForMax = random.nextDouble(1.0, 8.0)
            val randomModFreqForMin = random.nextDouble(1.0, 8.0)

            val doubleBuffer = DoubleArray(bufferSize) { i ->
                val t = (sampleIndex + i).toDouble() / sampleRate

                // Calcul du signal de modulation pour maxFreq, utilisant sa propre fréquence aléatoire
                val modulationSignalForMax = sin(2 * PI * randomModFreqForMax * t)

                // Calcul du signal de modulation pour minFreq, utilisant sa propre fréquence aléatoire
                val modulationSignalForMin = sin(2 * PI * randomModFreqForMin * t)

                // --- Signal basé sur maxFreq ---
                val instFreqMax = maxFreq + freqDev * modulationSignalForMax
                phaseMaxFreq += 2 * PI * instFreqMax / sampleRate
                if (phaseMaxFreq >= 2 * PI) phaseMaxFreq -= 2 * PI
                val signalMax = sin(phaseMaxFreq)

                // --- Signal basé sur minFreq (déviation FM inversée) ---
                // On utilise le signal de modulation inversé pour cette courbe
                val instFreqMin = minFreq + freqDev * (-modulationSignalForMin)
                phaseMinFreq += 2 * PI * instFreqMin / sampleRate
                if (phaseMinFreq >= 2 * PI) phaseMinFreq -= 2 * PI
                val signalMin = sin(phaseMinFreq)

                // Somme des deux signaux
                val combinedSignal = signalMin + signalMax

                // Normalisation
                (combinedSignal / 2.0).coerceIn(-1.0, 1.0)
            }

            sampleIndex += bufferSize

            val shortBuffer = ShortArray(bufferSize) { i ->
                (doubleBuffer[i] * Short.MAX_VALUE).toInt().toShort()
            }

            emit(shortBuffer)
        }
    }





}