package com.lebaillyapp.sonicjammer.di

import com.lebaillyapp.sonicjammer.audio.AudioPlayer
import com.lebaillyapp.sonicjammer.data.repository.JamAudioRepository
import com.lebaillyapp.sonicjammer.data.service.UltrasonicAudioService
import com.lebaillyapp.sonicjammer.data.service.UltrasonicAudioServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * ### AppModule
 *
 * Module Hilt centralisant la déclaration des dépendances de haut niveau.
 * Fournit notamment les instances nécessaires à l’injection de `JamAudioRepository` et son service associé.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * #### provideUltrasonicAudioService
     *
     * Fournit une instance de [UltrasonicAudioService] en injectant l'implémentation concrète.
     *
     * @return [UltrasonicAudioServiceImpl] castée en [UltrasonicAudioService]
     */
    @Provides
    @Singleton
    fun provideUltrasonicAudioService(): UltrasonicAudioService {
        return UltrasonicAudioServiceImpl()
    }

    /**
     * #### provideJamAudioRepository
     *
     * Fournit le repository `JamAudioRepository` en injectant le service audio nécessaire.
     *
     * @param audioService Instance fournie de [UltrasonicAudioService]
     * @return [JamAudioRepository] Singleton.
     */
    @Provides
    @Singleton
    fun provideJamAudioRepository(
        audioService: UltrasonicAudioService
    ): JamAudioRepository {
        return JamAudioRepository(audioService)
    }


    @Provides
    @Singleton
    fun provideAudioPlayer(): AudioPlayer {
        return AudioPlayer()
    }


}