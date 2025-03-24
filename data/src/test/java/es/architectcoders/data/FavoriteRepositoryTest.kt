package es.architectcoders.data

import es.architectcoders.data.datasource.ApodLocalDataSource
import es.architectcoders.data.datasource.RoversLocalDataSource
import es.architectcoders.data.repository.FavoriteRepository
import es.architectcoders.domain.Apod
import es.architectcoders.domain.Photo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class FavoriteRepositoryTest {
    @Mock
    lateinit var roversLocalDataSource: RoversLocalDataSource

    @Mock
    lateinit var apodLocalDataSource: ApodLocalDataSource

    private lateinit var favoriteRepository: FavoriteRepository

    private val sampleApod = Apod(
        id = "apod1",
        copyright = "NASA",
        date = "2023-01-01",
        explanation = "Explanation",
        hdurl = "https://example.com/hd",
        mediaType = "image",
        serviceVersion = "v1",
        title = "APOD Title",
        url = "https://example.com",
        favorite = true
    )

    private val samplePhoto = Photo(
        date = "2023-01-01",
        title = "Sample Photo",
        explanation = "A sample photo explanation.",
        hdurl = "https://example.com/hd",
        url = "https://example.com",
        mediaType = "image",
        serviceVersion = "v1",
        type = "photo",
        favorite = true,
        sol = "1",
        imgSrc = "https://example.com/image.jpg",
        id = "1",
        earthDate = "2023-01-01"
    )

    @Before
    fun setUp() {
        whenever(apodLocalDataSource.getFavoriteApods).thenReturn(flowOf(listOf(sampleApod)))
        whenever(roversLocalDataSource.getFavoritePhoto).thenReturn(flowOf(listOf(samplePhoto)))

        favoriteRepository = FavoriteRepository(roversLocalDataSource, apodLocalDataSource)
    }

    @Test
    fun `getFavoriteList returns combined favorite items`() = runBlocking {
        val favoriteItems = favoriteRepository.getFavoriteList().first()

        val expectedItems = listOf(sampleApod, samplePhoto)

        assertEquals(expectedItems, favoriteItems)
    }
}