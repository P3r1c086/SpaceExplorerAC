package es.architectcoders.data.repository

import es.architectcoders.data.datasource.ApodLocalDataSource
import es.architectcoders.data.datasource.RoversLocalDataSource
import es.architectcoders.domain.Apod
import es.architectcoders.domain.NasaItem
import es.architectcoders.domain.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val roversLocalDataSource: RoversLocalDataSource,
    private val apodLocalDataSource: ApodLocalDataSource
){

    fun getFavoriteList(): Flow<List<NasaItem>> {
        val allFavoritesApods: Flow<List<Apod>> = apodLocalDataSource.getFavoriteApods
        val allFavoritesRovers: Flow<List<Photo>> = roversLocalDataSource.getFavoritePhoto

        return combine(allFavoritesApods, allFavoritesRovers) { apods, rovers ->
            (apods + rovers).sortedByDescending { it.date }
        }
    }
}