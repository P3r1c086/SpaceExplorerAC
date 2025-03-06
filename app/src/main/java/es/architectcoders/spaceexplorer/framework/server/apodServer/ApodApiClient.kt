package es.architectcoders.spaceexplorer.framework.server.apodServer

import retrofit2.http.GET
import retrofit2.http.Query

interface ApodApiClient {

    @GET("planetary/apod")
    suspend fun getApod(
        @Query("api_key") apiKey: String
    ): ApodResponse
}