package es.architectcoders.usecases.rovers

import es.architectcoders.data.repository.RoversRepository
import es.architectcoders.usecases.SaveRoversFavoriteUseCase
import es.architectcoders.usecases.samplePhoto
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class SaveRoversFavoriteUseCaseTest {

    @Test
    fun `Invoke calls rovers repository`(): Unit = runBlocking {
        val photo = samplePhoto.copy(id = "1")
        val roversRepository = mock<RoversRepository>()
        val saveRoversFavoriteUseCase = SaveRoversFavoriteUseCase(roversRepository)

        saveRoversFavoriteUseCase(photo)

        verify(roversRepository).saveRoversAsFavourite(photo)
    }
}