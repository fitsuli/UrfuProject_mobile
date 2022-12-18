package ru.fitsuli.petsmobile.data.network

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.fitsuli.petsmobile.data.dto.LostAnimalEntity
import ru.fitsuli.petsmobile.data.dto.SignUpEntity
import ru.fitsuli.petsmobile.data.dto.UserEntity

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
    suspend fun getLostPets(): ApiResponse<List<LostAnimalEntity>>

}