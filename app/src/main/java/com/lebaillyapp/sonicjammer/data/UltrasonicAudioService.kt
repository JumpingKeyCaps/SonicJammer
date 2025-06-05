package com.lebaillyapp.sonicjammer.data

import com.lebaillyapp.sonicjammer.model.JamConfig
import kotlinx.coroutines.flow.Flow

/**
 * ### UltrasonicAudioService
 *
 * Interface defining the service responsible for generating an ultrasonic audio signal.
 *
 * Provides a flow of ShortArray audio buffers representing the generated signal to emit,
 * as well as a method to update the generation configuration.
 */
interface UltrasonicAudioService {

    /**
     *
     * #### updateConfig
     *
     * Updates the configuration used for signal generation.
     *
     * @param newConfig The new configuration to apply for signal generation.
     */
    fun updateConfig(newConfig: JamConfig)

    /**
     *
     * #### generateSignalFlow
     *
     * A continuous flow emitting audio buffers generated according to the current configuration.
     *
     * Each buffer is a ShortArray whose size is defined in the configuration.
     *
     * @return A Flow emitting ShortArray audio buffers to be consumed downstream.
     */
    fun generateSignalFlow(): Flow<ShortArray>
}