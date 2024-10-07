package es.architectcoders.data

import es.architectcoders.data.datasource.RoversLocalDataSource
import es.architectcoders.data.datasource.RoversRemoteDataSource
import es.architectcoders.data.repository.RoversRepository
import es.architectcoders.domain.Photo
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class RoversRepositoryTest{

    @Mock
    lateinit var roversLocalDataSource: RoversLocalDataSource

    @Mock
    lateinit var roversRemoteDataSource: RoversRemoteDataSource

    private lateinit var roversRepository: RoversRepository

    private val localPhotos = flowOf(listOf(samplePhoto.copy(id = "1")))

    @Before
    fun setUp() {
        whenever(roversLocalDataSource.getPhoto).thenReturn(localPhotos)
        roversRepository = RoversRepository(roversLocalDataSource, roversRemoteDataSource)
    }

    @Test
    fun `Photos are taken from local data source if available`(): Unit = runBlocking{

        val photos = roversRepository.allRovers

        assertEquals(localPhotos, photos)
    }

    @Test
    fun `Save as favorite marks as favorite an unfavorite photo`(): Unit = runBlocking{
        val photo = samplePhoto.copy(favorite = false)

        roversRepository.saveRoversAsFavourite(photo)

        verify(roversLocalDataSource).saveRovers(argThat { get(0).favorite })
    }

    @Test
    fun `Save as favorite marks as unfavorite an favorite photo`(): Unit = runBlocking{
        val photo = samplePhoto.copy(favorite = true)

        roversRepository.saveRoversAsFavourite(photo)

        verify(roversLocalDataSource).saveRovers(argThat { !get(0).favorite })
    }

    @Test
    fun `Save as favorite updates local data source`(): Unit = runBlocking{
        val photo = samplePhoto.copy(id = "2")

        roversRepository.saveRoversAsFavourite(photo)

        verify(roversLocalDataSource).saveRovers(argThat { get(0).id == "2" })
    }
}

private val samplePhoto = Photo(
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