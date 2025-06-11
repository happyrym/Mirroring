package com.rymin.mirroring

import android.content.Context
import com.example.mirroring.data.repository.MirroringRepository
import com.example.mirroring.data.repository.SettingsRepository
import com.example.mirroring.data.service.MirroringService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMirroringService(
        @ApplicationContext context: Context
    ): MirroringService {
        return MirroringService(context)
    }

    @Provides
    @Singleton
    fun provideMirroringRepository(
        @ApplicationContext context: Context,
        mirroringService: MirroringService
    ): MirroringRepository {
        return MirroringRepository(context, mirroringService)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        @ApplicationContext context: Context
    ): SettingsRepository {
        return SettingsRepository(context)
    }
}