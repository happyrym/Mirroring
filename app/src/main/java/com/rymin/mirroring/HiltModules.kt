package com.rymin.mirroring

import android.content.Context
import com.rymin.mirroring.repository.MirroringRepository
import com.rymin.mirroring.repository.SettingsRepository
import com.rymin.mirroring.service.MirroringService
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