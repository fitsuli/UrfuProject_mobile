package ru.fitsuli.petsmobile.data.dto.googlemap


import com.google.gson.annotations.SerializedName

data class GooglePlaceApiLookupResponse(
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("status")
    val status: String
)