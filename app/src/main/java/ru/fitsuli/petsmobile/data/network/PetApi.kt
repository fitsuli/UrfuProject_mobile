package ru.fitsuli.petsmobile.data.network

import com.skydoves.sandwich.ApiResponse
import okhttp3.RequestBody
import retrofit2.http.*
import ru.fitsuli.petsmobile.GOOGLE_MAP_API_KEY
import ru.fitsuli.petsmobile.data.dto.AnimalEntity
import ru.fitsuli.petsmobile.data.dto.SignUpEntity
import ru.fitsuli.petsmobile.data.dto.UserEntity
import ru.fitsuli.petsmobile.data.dto.googlemap.GooglePlaceApiLookupResponse

/**
 * Created by Dmitry Danilyuk at 22.11.2022
 */
interface PetApi {

    @POST("auth/signIn")
    suspend fun signIn(
        @Header("login") login: String, @Header("password") password: String
    ): ApiResponse<Unit>

    @POST("auth/signUp")
    @Headers("Content-Type: application/json")
    suspend fun signUp(@Body signUp: SignUpEntity): ApiResponse<Unit>

    @GET("users/me")
    suspend fun me(): ApiResponse<UserEntity>

    @GET("lostAnimals")
    suspend fun getLostPets(): ApiResponse<List<AnimalEntity>>

    @GET("lostAnimals/{id}")
    suspend fun getLostPetById(@Path("id") id: String): ApiResponse<AnimalEntity>

    @DELETE("lostAnimals/{id}")
    suspend fun closeLostPost(@Path("id") id: String): ApiResponse<Unit>

    @GET("foundAnimals")
    suspend fun getFoundPets(): ApiResponse<List<AnimalEntity>>

    @GET("foundAnimals/{id}")
    suspend fun getFoundPetById(@Path("id") id: String): ApiResponse<AnimalEntity>

    @DELETE("foundAnimals/{id}")
    suspend fun closeFoundPost(@Path("id") id: String): ApiResponse<Unit>

    @POST
    @Headers("Content-Type: multipart/form-data")
    suspend fun addLostPet(
        @Url url: String,
        @Body requestBody: RequestBody
    ): ApiResponse<Unit>

    // fetch latitude and longitude from Google Maps API
    @GET
    suspend fun getCoordinatesFromGoogle(
        @Url url: String = "https://maps.googleapis.com/maps/api/geocode/json",
        @Query("address") address: String,
        @Query("key") key: String = GOOGLE_MAP_API_KEY,
        @Query("language") language: String = "ru-RU"
    ): ApiResponse<GooglePlaceApiLookupResponse>
}