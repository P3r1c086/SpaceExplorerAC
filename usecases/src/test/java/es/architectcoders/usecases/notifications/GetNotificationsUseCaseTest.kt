package es.architectcoders.usecases.notifications

import es.architectcoders.usecases.GetNotificationsUseCase
import es.architectcoders.usecases.sampleNotificationsItem
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetNotificationsUseCaseTest{

    @Test
    fun `Invoke calls notifications repository`(): Unit = runBlocking {
        val notifications = flowOf(listOf(sampleNotificationsItem.copy(messageID = "023_AB_123")))
        val getNotificationsUseCase = GetNotificationsUseCase(mock {
            on{ allNotifications} doReturn notifications
        })

        val result = getNotificationsUseCase()

        assertEquals(notifications, result)
    }
}