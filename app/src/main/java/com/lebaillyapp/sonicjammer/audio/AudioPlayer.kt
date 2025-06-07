package com.lebaillyapp.sonicjammer.audio

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ### AudioPlayer
 *
 * Lecteur audio capable de jouer un flux continu d'échantillons audio PCM 16-bit 44.1kHz.
 *
 * Le player expose un [StateFlow] indiquant si la lecture est en cours, permettant à l'UI de s'adapter.
 *
 * @property sampleRate Fréquence d'échantillonnage (ex: 44100).
 * @property channelConfig Configuration du canal audio (ex: AudioFormat.CHANNEL_OUT_MONO).
 * @property audioFormat Format audio PCM (ex: AudioFormat.ENCODING_PCM_16BIT).
 */
class AudioPlayer(
    private val sampleRate: Int = 44100,
    private val channelConfig: Int = AudioFormat.CHANNEL_OUT_MONO,
    private val audioFormat: Int = AudioFormat.ENCODING_PCM_16BIT,
) {

    private var audioTrack: AudioTrack? = null
    private var playJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying

    /**
     * #### startPlaying
     *
     * Lance la lecture du flux audio fourni.
     * Si une lecture est déjà en cours, elle sera stoppée avant de démarrer la nouvelle.
     *
     * @param audioFlow Flux continu d'échantillons audio PCM 16-bit.
     */
    fun startPlaying(audioFlow: Flow<ShortArray>) {
        stopPlaying() // Stoppe la lecture en cours si nécessaire

        val minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat)
        audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            channelConfig,
            audioFormat,
            minBufferSize,
            AudioTrack.MODE_STREAM
        ).apply {
            play()
        }

        playJob = scope.launch {
            try {
                _isPlaying.value = true
                audioFlow.collect { buffer ->
                    audioTrack?.write(buffer, 0, buffer.size)
                }
            } catch (e: CancellationException) {
                // Lecture stoppée proprement
            } catch (e: Exception) {
                // Log erreur ou gérer selon besoin (ex: notifier UI)
                e.printStackTrace()
            } finally {
                _isPlaying.value = false
                audioTrack?.stop()
                audioTrack?.release()
                audioTrack = null
            }
        }
    }

    /**
     * #### stopPlaying
     *
     * Arrête la lecture en cours et libère les ressources associées.
     * L'état de lecture est mis à jour.
     */
    fun stopPlaying() {
        playJob?.cancel()
        playJob = null

        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null

        _isPlaying.value = false
    }

    /**
     * #### release
     *
     * Nettoie toutes les ressources, doit être appelé lors de la destruction de l'objet.
     */
    fun release() {
        stopPlaying()
        scope.cancel()
    }
}