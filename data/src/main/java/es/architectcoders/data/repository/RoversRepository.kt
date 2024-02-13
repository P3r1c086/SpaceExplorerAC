package es.architectcoders.data.repository

import es.architectcoders.data.datasource.RoversLocalDataSource
import es.architectcoders.data.datasource.RoversRemoteDataSource
import es.architectcoders.domain.Photo
import es.architectcoders.domain.Error
import javax.inject.Inject

class RoversRepository @Inject constructor(
private val roversLocalDataSource: RoversLocalDataSource,
private val roversRemoteDataSource: RoversRemoteDataSource){

    val allRovers get() = roversLocalDataSource.getPhoto

    suspend fun requestRovers(): Error? {
        if (roversLocalDataSource.isRoversEmpty()){
            val rovers = roversRemoteDataSource.getRovers()
            rovers.fold(ifLeft = { return it }) {
                     roversLocalDataSource.saveRovers(it)

            }
        }
        return null
    }

    suspend fun saveRoversAsFavourite(photo: Photo): Error? {
        val updatePhoto = photo.copy(favorite = !photo.favorite)
        return roversLocalDataSource.saveRovers(listOf(updatePhoto))
    }
}