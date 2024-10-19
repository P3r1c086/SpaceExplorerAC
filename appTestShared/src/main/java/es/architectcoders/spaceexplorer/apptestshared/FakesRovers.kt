package es.architectcoders.spaceexplorer.apptestshared

import arrow.core.right
import es.architectcoders.data.datasource.RoversLocalDataSource
import es.architectcoders.data.datasource.RoversRemoteDataSource
import es.architectcoders.domain.Photo
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar
import es.architectcoders.domain.Error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val samplePhoto = Photo(
    date = "2023-01-01",
    title = "Sample Photo",
    explanation = "A sample photo explanation.",
    hdurl = "https://example.com/hd",
    url = "https://example.com",
    mediaType = "image",
    serviceVersion = "v1",
    type = "photo",
    favorite = false,
    sol = "1",
    imgSrc = "https://example.com/image.jpg",
    id = "1",
    earthDate = "2023-01-01"
)

val defaultFakePhotos = listOf(
    samplePhoto.copy(id = "1"),
    samplePhoto.copy(id = "2"),
    samplePhoto.copy(id = "3"),
    samplePhoto.copy(id = "4")
)
val defaultFakePhotos2 = listOf(
    samplePhoto.copy(id = "1"),
    samplePhoto.copy(id = "2"),
    samplePhoto.copy(id = "3"),
    samplePhoto.copy(id = "4"),
    samplePhoto.copy(id = "5"),
    samplePhoto.copy(id = "6")
)

class FakeRoversLocalDataSource : RoversLocalDataSource {

    val inMemoryPhotos = MutableStateFlow<List<Photo>>(emptyList())

    override val getPhoto: MutableStateFlow<List<Photo>>
        get() = inMemoryPhotos

    override val getFavoritePhoto: Flow<List<Photo>>
        get() = inMemoryPhotos.map { photos ->
            photos.filter { it.favorite }
        }

    override suspend fun saveRovers(rovers: List<Photo>): Error? {
        inMemoryPhotos.value = rovers
        return null
    }

    override suspend fun isRoversEmpty() = getPhoto.value.isEmpty()
}

class FakeRoversRemoteDataSource : RoversRemoteDataSource {

    var photos = defaultFakePhotos

    override suspend fun getRovers(date: Calendar) = photos.right()
}

