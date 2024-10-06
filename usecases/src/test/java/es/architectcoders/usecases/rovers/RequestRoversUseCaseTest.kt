package es.architectcoders.usecases.rovers

import es.architectcoders.data.repository.RoversRepository
import es.architectcoders.usecases.RequestRoversUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RequestRoversUseCaseTest {

    @Test
    fun `Invoke calls rovers repository`(): Unit = runBlocking {
        val roversRepository = mock<RoversRepository>()
        val requestRoversUseCase = RequestRoversUseCase(roversRepository)

        requestRoversUseCase()

        verify(roversRepository).requestRovers()
    }
}