package es.architectcoders.spaceexplorer.framework.server.notificationsServer

import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationsApiClient {

    @GET("DONKI/notifications")
    suspend fun getNotifications(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("type") type: String,
        @Query("api_key") apiKey: String
    ): List<NotificationsItemResponse>
}