package prog7314.poe.edubridge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import prog7314.poe.edubridge.BuildConfig
import prog7314.poe.edubridge.data.remote.WeatherApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

    private const val WEATHER_BASE_URL = "https://api.openweathermap.org/"

    @Provides
    @Singleton
    @Named("weatherRetrofit")
    fun provideWeatherRetrofit(
        @Named("plainClient") client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(WEATHER_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideWeatherApi(
        @Named("weatherRetrofit") retrofit: Retrofit    // ← add @Named
    ): WeatherApi = retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    @Named("weatherApiKey")
    fun provideWeatherApiKey(): String = BuildConfig.WEATHER_API_KEY
}