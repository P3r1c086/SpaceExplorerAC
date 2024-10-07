package es.architectcoders.usecases.rovers

import es.architectcoders.usecases.GetRoversUseCase
import es.architectcoders.usecases.samplePhoto
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetRoversUseCaseTest {

    @Test
    fun `Invoke calls rovers repository`(): Unit = runBlocking {
        val photos = flowOf(listOf(samplePhoto.copy(id = "1")))
        val getRoversUseCase = GetRoversUseCase(mock {
            on{ allRovers} doReturn photos
        })

        val result = getRoversUseCase()

        assertEquals(photos, result)
    }
}

