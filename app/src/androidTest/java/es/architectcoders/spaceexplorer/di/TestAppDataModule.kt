package es.architectcoders.spaceexplorer.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import es.architectcoders.data.datasource.ApodLocalDataSource
import es.architectcoders.data.datasource.ApodRemoteDataSource
import es.architectcoders.data.datasource.NotificationsLocalDataSource
import es.architectcoders.data.datasource.NotificationsRemoteDataSource
import es.architectcoders.data.datasource.RoversLocalDataSource
import es.architectcoders.data.datasource.RoversRemoteDataSource
import es.architectcoders.spaceexplorer.BuildConfig
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.apptestshared.FakeApodLocalDataSource
import es.architectcoders.spaceexplorer.apptestshared.FakeApodRemoteDataSource
import es.architectcoders.spaceexplorer.apptestshared.FakeNotificationsLocalDataSource
import es.architectcoders.spaceexplorer.apptestshared.FakeNotificationsRemoteDataSource
import es.architectcoders.spaceexplorer.apptestshared.FakeRoversLocalDataSource
import es.architectcoders.spaceexplorer.apptestshared.FakeRoversRemoteDataSource
import es.architectcoders.spaceexplorer.framework.database.AppDatabase
import es.architectcoders.spaceexplorer.framework.server.NasaInterceptor
import es.architectcoders.spaceexplorer.framework.server.apodServer.ApodApiClient
import es.architectcoders.spaceexplorer.framework.server.notificationsServer.NotificationsApiClient
import es.architectcoders.spaceexplorer.framework.server.roverServer.RoversApiClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppDataModule::class, DatabaseModule::class]
)
object TestAppDataModule {

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
    fun provideFakeLocalDataSource(): ApodLocalDataSource = FakeApodLocalDataSource()

//    @Provides
//    @Singleton
//    fun provideFakeRemoteDataSource(): ApodRemoteDataSource = FakeApodRemoteDataSource()

//    @Provides
//    @Singleton
//    fun provideFakeRemoteDataSourceRovers(): RoversRemoteDataSource = FakeRoversRemoteDataSource()

    @Provides
    @Singleton
    fun provideFakeLocalDataSourceRovers(): RoversLocalDataSource = FakeRoversLocalDataSource()

//    @Provides
//    @Singleton
//    fun provideFakeRemoteDataSourceNotifications(): NotificationsRemoteDataSource = FakeNotificationsRemoteDataSource()

    @Provides
    @Singleton
    fun provideFakeLocalDataSourceNotifications(): NotificationsLocalDataSource = FakeNotificationsLocalDataSource()

    @Provides
    @Singleton
    fun provideRetrofit(nasaClient: OkHttpClient, @ApiUrl apiUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
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

    @Provides
    @Singleton
    @ApiKey
    fun provideApiKey(app: Application): String = app.getString(R.string.api_key)
}
