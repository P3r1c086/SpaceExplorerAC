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
import es.architectcoders.spaceexplorer.apptestshared.FakeApodLocalDataSource
import es.architectcoders.spaceexplorer.apptestshared.FakeApodRemoteDataSource
import es.architectcoders.spaceexplorer.apptestshared.FakeNotificationsLocalDataSource
import es.architectcoders.spaceexplorer.apptestshared.FakeNotificationsRemoteDataSource
import es.architectcoders.spaceexplorer.apptestshared.FakeRoversLocalDataSource
import es.architectcoders.spaceexplorer.apptestshared.FakeRoversRemoteDataSource
import es.architectcoders.spaceexplorer.framework.database.AppDatabase
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppDataModule::class, DatabaseModule::class]
)
object TestAppDataModule {

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

    @Provides
    @Singleton
    fun provideFakeRemoteDataSource(): ApodRemoteDataSource = FakeApodRemoteDataSource()

    @Provides
    @Singleton
    fun provideFakeRemoteDataSourceRovers(): RoversRemoteDataSource = FakeRoversRemoteDataSource()

    @Provides
    @Singleton
    fun provideFakeLocalDataSourceRovers(): RoversLocalDataSource = FakeRoversLocalDataSource()

    @Provides
    @Singleton
    fun provideFakeRemoteDataSourceNotifications(): NotificationsRemoteDataSource = FakeNotificationsRemoteDataSource()

    @Provides
    @Singleton
    fun provideFakeLocalDataSourceNotifications(): NotificationsLocalDataSource = FakeNotificationsLocalDataSource()


}
