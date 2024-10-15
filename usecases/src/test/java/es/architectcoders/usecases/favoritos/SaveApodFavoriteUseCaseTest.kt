package es.architectcoders.usecases.favoritos

import es.architectcoders.data.repository.ApodRepository
import es.architectcoders.usecases.SaveApodFavoriteUseCase
import es.architectcoders.usecases.sampleApod
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class SaveApodFavoriteUseCaseTest {

    @Test
    fun `Invoke calls apod repository`(): Unit = runBlocking {
        val apod = sampleApod.copy(id = "1")
        val apodRepository = mock<ApodRepository>()
        val saveApodFavoriteUseCase = SaveApodFavoriteUseCase(apodRepository)

        saveApodFavoriteUseCase(apod)

        verify(apodRepository).saveApodAsFavourite(apod)
    }
}