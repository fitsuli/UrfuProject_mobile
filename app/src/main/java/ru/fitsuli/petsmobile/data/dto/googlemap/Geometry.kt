package ru.fitsuli.petsmobile.data.dto.googlemap


import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("bounds")
    val bounds: Bounds,
    @SerializedName("location")
    val location: Location,
    @SerializedName("location_type")
    val locationType: String,
    @SerializedName("viewport")
    val viewport: Viewport
)