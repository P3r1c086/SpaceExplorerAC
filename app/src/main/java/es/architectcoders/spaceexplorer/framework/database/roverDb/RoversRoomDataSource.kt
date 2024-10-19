package es.architectcoders.spaceexplorer.framework.database.roverDb

import es.architectcoders.data.datasource.RoversLocalDataSource
import es.architectcoders.domain.Error
import es.architectcoders.domain.Photo
import es.architectcoders.spaceexplorer.framework.tryCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import es.architectcoders.spaceexplorer.framework.database.roverDb.PhotoEntity as DbPhoto

class RoversRoomDataSource @Inject constructor(
    private val roversDao: RoversDao
) : RoversLocalDataSource {

    override val getPhoto: Flow<List<Photo>> = roversDao.getAllRovers().map {
        it.toDomain()
    }

    override val getFavoritePhoto: Flow<List<Photo>> = roversDao.getAllRovers().map { listaPhotos ->
        listaPhotos.filter { elementoDelistaPhotos-> elementoDelistaPhotos.favorite }.toDomain()
    }

    override suspend fun saveRovers(rovers: List<Photo>): Error? = tryCall {
        roversDao.insertRovers(rovers.fromDomain())
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override suspend fun isRoversEmpty(): Boolean = roversDao.roversCount() == 0
}

private fun List<DbPhoto>.toDomain(): List<Photo> = map { it.toDomain() }

fun DbPhoto.toDomain(): Photo =
    Photo(
        earthDate = earthDate,
        id = id.toString(),
        imgSrc = imgSrc,
        sol = sol,
        favorite = favorite,
        type = "rover",
        serviceVersion = "",
        title = "",
        explanation = "",
        hdurl = "",
        url = "",
        mediaType = "",
        date = ""
    )

fun List<Photo>.fromDomain(): List<DbPhoto> = map { it.fromDomain() }

private fun Photo.fromDomain(): DbPhoto =
    DbPhoto(
        earthDate = earthDate,
        id = id.toInt(),
        imgSrc = imgSrc,
        sol = sol,
        favorite = favorite,
        type = "rover",
        serviceVersion = "",
        title = "",
        explanation = "",
        hdurl = "",
        url = "",
        mediaType = "",
        date = ""
    )


