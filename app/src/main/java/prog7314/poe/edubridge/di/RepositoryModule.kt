package prog7314.poe.edubridge.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Repositories use constructor injection — no @Provides methods are needed here.
 * All config values (e.g. weather API key) are provided by their respective modules.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule