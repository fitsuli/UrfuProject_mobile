package ru.fitsuli.petsmobile.utils

import android.content.Context
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

/**
 * Created by Dmitry Danilyuk at 22.11.2022
 */
private val httpConnectTimeoutMs = 10.seconds.toJavaDuration()
private val httpWriteTimeoutMs = 10.seconds.toJavaDuration()
private val httpReadTimeoutMs = 10.seconds.toJavaDuration()

const val userAgent = "Pets Mobile app"

fun newHttpClient(context: Context): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    return OkHttpClient.Builder()
//        .retryOnConnectionFailure(true)
        .setCookieStore(context)
        .addNetworkInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", userAgent)
                .build()
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)
        .cache(null)
        .connectTimeout(httpConnectTimeoutMs)
        .writeTimeout(httpWriteTimeoutMs)
        .readTimeout(httpReadTimeoutMs)
        .build()
}

fun <T> newRetrofit(context: Context, baseUrl: String, service: Class<T>): T {

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
        .client(newHttpClient(context))
        .build()

    return retrofit.create(service)
}