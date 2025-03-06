package es.architectcoders.spaceexplorer.framework.server.apodServer

import arrow.core.Either
import es.architectcoders.data.datasource.ApodRemoteDataSource
import es.architectcoders.domain.Apod
import es.architectcoders.domain.Error
import es.architectcoders.spaceexplorer.di.ApiKey
import es.architectcoders.spaceexplorer.framework.server.roverServer.RemoteConnection
import es.architectcoders.spaceexplorer.framework.toDomain
import es.architectcoders.spaceexplorer.framework.tryCall
import javax.inject.Inject

class ApodServerDataSource @Inject constructor(@ApiKey private val apiKey: String) :
    ApodRemoteDataSource {

    override suspend fun getApod(): Either<Error, Apod?> = tryCall {
        RemoteConnection.serviceApod
            .getApod(apiKey)
            .toDomain()
    }
}