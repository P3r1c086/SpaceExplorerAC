package es.architectcoders.usecases.notifications

import es.architectcoders.data.repository.NotificationsRepository
import es.architectcoders.usecases.RequestNotificationsUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RequestNotificationsUseCaseTest{

    @Test
    fun `Invoke calls notifications repository`(): Unit = runBlocking {
        val notificationsRepository = mock<NotificationsRepository>()
        val requestNotificationsUseCase = RequestNotificationsUseCase(notificationsRepository)

        requestNotificationsUseCase()

        verify(notificationsRepository).requestNotifications()
    }
}