package es.architectcoders.spaceexplorer.framework.database.apodDb

import es.architectcoders.data.datasource.ApodLocalDataSource
import es.architectcoders.domain.Apod
import es.architectcoders.domain.Error
import es.architectcoders.spaceexplorer.framework.toDomain
import es.architectcoders.spaceexplorer.framework.toEntity
import es.architectcoders.spaceexplorer.framework.toError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApodRoomDataSource @Inject constructor(
    private val apodDao: ApodDao
) : ApodLocalDataSource {

    override val getApods: Flow<List<Apod>> = apodDao.getAllApods().map { apodList ->
        apodList.map { apodEntity -> apodEntity.toDomain() }
    }

    override val getFavoriteApods: Flow<List<Apod>> = apodDao.getAllApods().map { apodList ->
        apodList.filter { apod -> apod.favorite }.map { apodEntity -> apodEntity.toDomain() }
    }

    override suspend fun saveApod(apod: Apod?): Error? = try {
        apod?.toEntity()?.let { apodEntity ->  apodDao.insertApod(apodEntity) }
        null
    } catch (e: Exception) {
        e.toError()
    }

    override suspend fun isApodEmpty(): Boolean  = apodDao.apodsCount() == 0

    override suspend fun apodExists(apod: Apod?): Boolean {
        return apod?.toEntity()?.date?.let { date -> apodDao.findApodByDate(date) } != null
    }

    override suspend fun saveApodAsFavourite(apod: Apod?): Error? = try {
        apod?.toEntity()?.let { apodEntity -> apodDao.updateApod(apodEntity) }
        null
    } catch (e: Exception) {
        e.toError()
    }
}