package ru.fitsuli.petsmobile.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Dmitry Danilyuk at 22.11.2022
 */
data class SignUpEntity(
    @SerializedName("FullName") val fullName: String = "",
    @SerializedName("Login") val login: String = "",
    @SerializedName("Password") val password: String = "",
    @SerializedName("Role") val role: String = "User",
)
