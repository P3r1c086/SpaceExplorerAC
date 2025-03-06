package es.architectcoders.spaceexplorer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.architectcoders.data.datasource.ApodLocalDataSource
import es.architectcoders.data.datasource.ApodRemoteDataSource
import es.architectcoders.data.datasource.NotificationsLocalDataSource
import es.architectcoders.data.datasource.NotificationsRemoteDataSource
import es.architectcoders.data.datasource.RoversLocalDataSource
import es.architectcoders.data.datasource.RoversRemoteDataSource
import es.architectcoders.spaceexplorer.framework.database.apodDb.ApodRoomDataSource
import es.architectcoders.spaceexplorer.framework.database.notificationsDb.NotificationsRoomDataSource
import es.architectcoders.spaceexplorer.framework.database.roverDb.RoversRoomDataSource
import es.architectcoders.spaceexplorer.framework.server.apodServer.ApodServerDataSource
import es.architectcoders.spaceexplorer.framework.server.notificationsServer.NotificationsServerDataSource
import es.architectcoders.spaceexplorer.framework.server.roverServer.RoversServerDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule {

    @Binds
    abstract fun bindLocalDataSource(localDataSource: ApodRoomDataSource): ApodLocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSource: ApodServerDataSource): ApodRemoteDataSource

    @Binds
    abstract fun bindRemoteDataSourceRovers(remoteDataSource: RoversServerDataSource): RoversRemoteDataSource

    @Binds
    abstract fun bindLocalDataSourceRovers(localDataSource: RoversRoomDataSource): RoversLocalDataSource

    @Binds
    abstract fun bindRemoteDataSourceNotifications(remoteDataSource: NotificationsServerDataSource):
            NotificationsRemoteDataSource

    @Binds
    abstract fun bindLocalDataSourceNotifications(localDataSource: NotificationsRoomDataSource) :
            NotificationsLocalDataSource
}