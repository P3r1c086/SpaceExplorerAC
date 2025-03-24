package es.architectcoders.spaceexplorer.apptestshared

import es.architectcoders.data.datasource.ApodLocalDataSource
import es.architectcoders.domain.Apod
import es.architectcoders.spaceexplorer.framework.database.apodDb.ApodEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

private val defaultFakeApod = Apod(
    id = "1",
    copyright = "NASA",
    date = "2023-01-01",
    explanation = "A sample APOD explanation.",
    hdurl = "https://example.com/hd_apod",
    mediaType = "image",
    serviceVersion = "v1",
    title = "Sample APOD",
    url = "https://example.com/apod",
    favorite = false,
    type = "apod"
)
val defaultFakeApodEntity = ApodEntity(
    id = 1,
    copyright = "NASA",
    date = "2023-01-01",
    explanation = "A sample APOD explanation.",
    hdurl = "https://example.com/hd_apod",
    mediaType = "image",
    serviceVersion = "v1",
    title = "Sample APOD",
    url = "https://example.com/apod",
    favorite = false
)
val defaultFakeApodEntity2 = ApodEntity(
    id = 2,
    copyright = "NASA",
    date = "2023-02-02",
    explanation = "A sample APOD explanation.",
    hdurl = "https://example.com/hd_apod",
    mediaType = "image",
    serviceVersion = "v1",
    title = "Sample APOD",
    url = "https://example.com/apod",
    favorite = false
)

class FakeApodLocalDataSource : ApodLocalDataSource {

    private val inMemoryApods = MutableStateFlow<List<Apod>>(emptyList())

    override val getApods: Flow<List<Apod>>
        get() = inMemoryApods

    override val getFavoriteApods: Flow<List<Apod>>
        get() = inMemoryApods.map { apods ->
            apods.filter { it.favorite }
        }

    override suspend fun saveApod(apod: Apod?): es.architectcoders.domain.Error? {
        apod?.let {
            inMemoryApods.value = inMemoryApods.value + it
        }
        return null
    }

    override suspend fun isApodEmpty(): Boolean = inMemoryApods.value.isEmpty()

    override suspend fun apodExists(apod: Apod?): Boolean {
        return apod != null && inMemoryApods.value.any { it.id == apod.id }
    }

    override suspend fun saveApodAsFavourite(apod: Apod?): es.architectcoders.domain.Error? {
        apod?.let {
            val updatedApods = inMemoryApods.value.map { existingApod ->
                if (existingApod.id == apod.id) {
                    existingApod.copy(favorite = true)
                } else {
                    existingApod
                }
            }
            inMemoryApods.value = updatedApods
        }
        return null
    }
}

//class FakeApodRemoteDataSource : ApodRemoteDataSource {
//
//    override suspend fun getApod(): Either<Error, Apod?> {
//        // Simulamos la respuesta con un Apod de ejemplo
//        return Either.Right(defaultFakeApod)
//    }
//}