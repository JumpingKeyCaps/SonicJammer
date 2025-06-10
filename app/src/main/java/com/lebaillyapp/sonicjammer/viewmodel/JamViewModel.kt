package com.lebaillyapp.sonicjammer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lebaillyapp.sonicjammer.audio.AudioPlayer
import com.lebaillyapp.sonicjammer.data.repository.JamAudioRepository
import com.lebaillyapp.sonicjammer.model.JamConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ### JamViewModel
 *
 * ViewModel gérant la génération et la lecture du signal audio ultrasonique.
 * Il coordonne la configuration, la génération du flux audio, et le contrôle de la lecture.
 *
 * @property repository Le repository fournissant le flux audio modélisé.
 * @property audioPlayer Le lecteur audio jouant le flux audio.
 */
@HiltViewModel
class JamViewModel @Inject constructor(
    private val repository: JamAudioRepository,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    private val _config = MutableStateFlow(JamConfig())
    val config: StateFlow<JamConfig> = _config.asStateFlow()

    /**
     * StateFlow indiquant si la lecture est en cours.
     */
    val isPlaying: StateFlow<Boolean> = audioPlayer.isPlaying

    init {
        // Synchronise la config vers le repository quand elle change
        viewModelScope.launch {
            _config.collect { newConfig ->
                repository.updateConfig(newConfig)
            }
        }
    }

    /**
     * Met à jour la configuration du signal.
     *
     * @param newConfig Nouvelle configuration à appliquer.
     */
    fun updateConfig(newConfig: JamConfig) {
        _config.value = newConfig
    }

    /**
     * Démarre la lecture audio du flux généré.
     */
    fun startPlaying() {
        viewModelScope.launch {
            audioPlayer.startPlaying(repository.getSignalFlow())
        }
    }

    /**
     * Arrête la lecture audio.
     */
    fun stopPlaying() {
        viewModelScope.launch {
            audioPlayer.stopPlaying()
        }
    }
}