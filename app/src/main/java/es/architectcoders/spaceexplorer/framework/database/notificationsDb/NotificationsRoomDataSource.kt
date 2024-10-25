package es.architectcoders.spaceexplorer.framework.database.notificationsDb

import es.architectcoders.data.datasource.NotificationsLocalDataSource
import es.architectcoders.domain.Error
import es.architectcoders.domain.NotificationsItem
import es.architectcoders.spaceexplorer.framework.database.notificationsDb.NotificationsItemEntity
import es.architectcoders.spaceexplorer.framework.tryCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import es.architectcoders.spaceexplorer.framework.database.notificationsDb.NotificationsItemEntity as DbNotificationsItem

class NotificationsRoomDataSource @Inject constructor(
    private val notificationsDao: NotificationsDao,
) : NotificationsLocalDataSource {

    override val getNotifications: Flow<List<NotificationsItem>> =
        notificationsDao.getAllNotifications().map {
            it.toDomain() }

    override suspend fun saveNotifications(notifications: List<NotificationsItem>): Error? = tryCall {
        notificationsDao.insertNotifications(
            notifications.fromDomain())
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override suspend fun isNotificationsEmpty(): Boolean = notificationsDao.notificationsCount() == 0

}
private fun List<DbNotificationsItem>.toDomain(): List<NotificationsItem> = map {
    it.toDomain() }


private fun DbNotificationsItem.toDomain(): NotificationsItem =
    NotificationsItem(
        messageIssueTime,
        messageID,
        messageURL,
        messageBody,
        messageType
    )

fun List<NotificationsItem>.fromDomain(): List<DbNotificationsItem> = map { it.fromDomain() }

private fun NotificationsItem.fromDomain(): DbNotificationsItem =
    DbNotificationsItem(
        id = 0,
        messageIssueTime,
        messageID,
        messageURL,
        messageBody,
        messageType
    )
