package es.architectcoders.spaceexplorer.framework.server.roverServer

import com.google.gson.annotations.SerializedName

data class RoversResponse(
    val photos: List<PhotoResponse>
)

data class PhotoResponse(
    @SerializedName("earth_date") val earthDate: String,
    val id: Int,
    @SerializedName("img_src") val imgSrc: String,
    val sol: Int
)

data class CameraResponse(
    @SerializedName("full_name") val fullName: String,
    val id: Int,
    val name: String,
    @SerializedName("rover_id") val roverId: Int
)

data class RoverResponse(
    val cameras: List<CameraXResponse>,
    val id: Int,
    @SerializedName("landing_date") val landingDate: String,
    @SerializedName("launch_date") val launchDate: String,
    @SerializedName("max_date") val maxDate: String,
    @SerializedName("max_sol") val maxSol: Int,
    val name: String,
    val status: String,
    @SerializedName("total_photos") val totalPhotos: Int
)

data class CameraXResponse(
    @SerializedName("full_name") val fullName: String,
    val name: String
)