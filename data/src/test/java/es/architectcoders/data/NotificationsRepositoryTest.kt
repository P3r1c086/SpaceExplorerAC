package es.architectcoders.data

import es.architectcoders.data.datasource.NotificationsLocalDataSource
import es.architectcoders.data.datasource.NotificationsRemoteDataSource
import es.architectcoders.data.repository.NotificationsRepository
import es.architectcoders.domain.NotificationsItem
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class NotificationsRepositoryTest{

    @Mock
    lateinit var notificationsLocalDataSource: NotificationsLocalDataSource

    @Mock
    lateinit var notificationsRemoteDataSource: NotificationsRemoteDataSource

    private lateinit var notificationsRepository: NotificationsRepository

    private val localNotifications = flowOf(listOf(sampleNotification.copy(messageID = "023_AB_123")))

    @Before
    fun setUp() {
        whenever(notificationsLocalDataSource.getNotifications).thenReturn(localNotifications)
        notificationsRepository = NotificationsRepository(
            notificationsLocalDataSource, notificationsRemoteDataSource
        )
    }

    @Test
    fun `Notifications are taken from local data source if available`(): Unit = runBlocking{

        val notifications = notificationsRepository.allNotifications

        assertEquals(localNotifications, notifications)
    }

}

private val sampleNotification = NotificationsItem(
    "2023-01-01",
    "023_AB_123",
    "",
    "https://example.com",
    "FLR"
)