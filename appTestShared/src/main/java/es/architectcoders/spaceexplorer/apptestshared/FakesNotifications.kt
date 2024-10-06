package es.architectcoders.spaceexplorer.apptestshared

import arrow.core.right
import es.architectcoders.data.datasource.NotificationsLocalDataSource
import es.architectcoders.data.datasource.NotificationsRemoteDataSource
import es.architectcoders.domain.NotificationsItem
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar
import es.architectcoders.domain.Error

val sampleNotificationItem = NotificationsItem(
    "2014-05-08T12:43Z",
    "023_AB_123",
    "## NASA Goddard Space Flight Center, Space Weather Research Center ( SWRC )## Message Type: Space Weather Notification - M5.2 Flare#### Message Issue Date: 2014-05-08T12:43:04Z## Message ID: 20140508-AL-002## Disclaimer: NOAA's Space Weather Prediction Center (http://swpc.noaa.gov) is the United States Government official source for space weather forecasts. This Experimental Research Information",
    "https://kauai.ccmc.gsfc.nasa.gov/DONKI/view/Alert/5429/1",
    "FLR"
)

val defaultFakeNotifications = listOf(
    sampleNotificationItem.copy(messageID = "1"),
    sampleNotificationItem.copy(messageID = "2"),
    sampleNotificationItem.copy(messageID = "3"),
    sampleNotificationItem.copy(messageID = "4")
)

class FakeNotificationsLocalDataSource : NotificationsLocalDataSource {

    val inMemoryNotifications = MutableStateFlow<List<NotificationsItem>>(emptyList())
    override val getNotifications: MutableStateFlow<List<NotificationsItem>>
        get() = inMemoryNotifications

    override suspend fun saveNotifications(notifications: List<NotificationsItem>): Error? {
        inMemoryNotifications.value = notifications
        return null
    }

    override suspend fun isNotificationsEmpty() = getNotifications.value.isEmpty()
}

class FakeNotificationsRemoteDataSource : NotificationsRemoteDataSource {

    var notifications = defaultFakeNotifications
    override suspend fun getNotifications(date: Calendar) = notifications.right()
}