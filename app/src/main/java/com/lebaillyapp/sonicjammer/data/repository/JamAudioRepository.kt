package com.lebaillyapp.sonicjammer.data.repository

import com.lebaillyapp.sonicjammer.data.service.UltrasonicAudioService
import com.lebaillyapp.sonicjammer.model.JamConfig
import kotlinx.coroutines.flow.Flow

/**
 * ### JamAudioRepository
 *
 * Repository chargé d'exposer le flux de signal audio généré à partir de la configuration en cours.
 * Il encapsule l'accès au service `UltrasonicAudioService` et permet une séparation claire des couches.
 *
 * @property audioService Instance de [UltrasonicAudioService] utilisée pour générer le signal.
 */
class JamAudioRepository(
    private val audioService: UltrasonicAudioService
) {

    /**
     * #### updateConfig
     *
     * Met à jour dynamiquement la configuration du signal jamming.
     *
     * @param newConfig Nouvelle configuration à appliquer au service.
     */
    fun updateConfig(newConfig: JamConfig) {
        audioService.updateConfig(newConfig)
    }

    /**
     * #### getSignalFlow
     *
     * Expose le flux d’échantillons audio bruts (`ShortArray`) générés selon la configuration actuelle.
     *
     * @return Un [Flow] continu de buffers audio courts à envoyer au système de lecture audio.
     */
    fun getSignalFlow(): Flow<ShortArray> = audioService.generateSignalFlow()
}