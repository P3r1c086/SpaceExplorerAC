package es.architectcoders.spaceexplorer.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.framework.database.AppDatabase
import es.architectcoders.spaceexplorer.framework.server.NasaInterceptor
import es.architectcoders.spaceexplorer.framework.server.apodServer.ApodApiClient
import es.architectcoders.spaceexplorer.framework.server.notificationsServer.NotificationsApiClient
import es.architectcoders.spaceexplorer.framework.server.roverServer.RoversApiClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class, RetrofitModule::class]
)
object TestAppDataModule {

    @Provides
    @Singleton
    @ApiKey
    fun provideApiKey(app: Application): String = app.getString(R.string.api_key)

    @Provides
    @Singleton
    @ApiUrl
    fun provideApiUrl(): String = "https://localhost:8080"

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room.inMemoryDatabaseBuilder(
        app,
        AppDatabase::class.java
    ).build()

    @Provides
    @Singleton
    fun provideRoversDao(db: AppDatabase) = db.getRoversDao()
    @Provides
    @Singleton
    fun provideApodDao(db: AppDatabase) = db.getApodDao()
    @Provides
    @Singleton
    fun provideNotificationsDao(db: AppDatabase) = db.getNotificationsDao()

    @Provides
    @Singleton
    fun provideRetrofit(nasaClient: OkHttpClient, @ApiUrl apiUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(nasaClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApodApiClient(retrofit: Retrofit): ApodApiClient {
        return retrofit.create(ApodApiClient::class.java)
    }

    @Provides
    @Singleton
    fun provideRoversApiClient(retrofit: Retrofit): RoversApiClient {
        return retrofit.create(RoversApiClient::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationsApiClient(retrofit: Retrofit): NotificationsApiClient {
        return retrofit.create(NotificationsApiClient::class.java)
    }

    @Provides
    @Singleton
    fun provideClient(interceptor: NasaInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}
