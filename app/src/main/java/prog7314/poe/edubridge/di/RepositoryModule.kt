package prog7314.poe.edubridge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import prog7314.poe.edubridge.BuildConfig
import prog7314.poe.edubridge.data.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // Repositories use constructor injection — no @Provides needed.
    // This module only provides external config values.

    @Provides
    @Singleton
    fun provideWeatherApiKey(): String = BuildConfig.WEATHER_API_KEY
}