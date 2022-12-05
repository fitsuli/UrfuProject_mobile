package ru.fitsuli.petsmobile.utils

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

/**
 * Created by Dmitry Danilyuk at 22.11.2022
 */

private const val cookiesKey = "app_cookies"

class SendSavedCookiesInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(context)
            .getStringSet(cookiesKey, hashSetOf())
            .orEmpty().toHashSet()

        preferences.forEach {
            builder.addHeader("Cookie", it)
        }

        return chain.proceed(builder.build())
    }
}

class SaveReceivedCookiesInterceptor(private val context: Context) : Interceptor {

    //    @JvmField
    private val setCookieHeader = "Set-Cookie"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        if (originalResponse.headers(setCookieHeader).isNotEmpty()) {
            val cookies = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getStringSet(cookiesKey, hashSetOf()).orEmpty().toHashSet()

            originalResponse.headers(setCookieHeader).forEach {
                cookies.add(it)
            }

            PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit {
                    putStringSet(cookiesKey, cookies)
                }
        }

        return originalResponse
    }

}

fun OkHttpClient.Builder.setCookieStore(context: Context): OkHttpClient.Builder {
    return this
        .addInterceptor(SendSavedCookiesInterceptor(context))
        .addInterceptor(SaveReceivedCookiesInterceptor(context))
}