package prog7314.poe.edubridge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import prog7314.poe.edubridge.BuildConfig
import prog7314.poe.edubridge.data.remote.AuthInterceptor
import prog7314.poe.edubridge.data.remote.EduBridgeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TIMEOUT_SECONDS = 30L

    // ── Logging ─────────────────────────────────────────
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

    // ── Authenticated OkHttp (main API) ─────────────────
    @Provides
    @Singleton
    @Named("authClient")
    fun provideAuthOkHttpClient(
        auth: AuthInterceptor,
        logging: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(auth)
        .addInterceptor(logging)
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    // ── Plain OkHttp (weather, no auth) ─────────────────
    @Provides
    @Singleton
    @Named("plainClient")
    fun providePlainOkHttpClient(
        logging: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    // ── Retrofit for the EduBridge API ──────────────────
    @Provides
    @Singleton
    @Named("eduBridgeRetrofit")
    fun provideRetrofit(
        @Named("authClient") client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideEduBridgeApi(
        @Named("eduBridgeRetrofit") retrofit: Retrofit   // ← add @Named
    ): EduBridgeApi = retrofit.create(EduBridgeApi::class.java)
}