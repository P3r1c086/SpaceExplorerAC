package es.architectcoders.spaceexplorer.framework.server.roverServer

import es.architectcoders.spaceexplorer.framework.server.apodServer.ApodApiClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RemoteConnection {

    private val okHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }

    private val builder = Retrofit.Builder()
        .baseUrl("https://api.nasa.gov/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: RoversApiClient = builder.create()
    val serviceApod: ApodApiClient = builder.create()
}