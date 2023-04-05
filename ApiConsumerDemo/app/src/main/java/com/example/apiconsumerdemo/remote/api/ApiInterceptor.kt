package com.example.apiconsumerdemo.remote.api

import android.os.Build.VERSION.SDK_INT
import okhttp3.Interceptor
import okhttp3.Response

internal class ApiInterceptor : Interceptor {

    companion object {
        internal const val HEADER_USER_AGENT = "User-Agent"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val urlBuilder = request.url().newBuilder().apply {
            scheme("https")
            //query params - some APIs require authentication
        }

        val builder = request.newBuilder().url(urlBuilder.build()).apply {
            header(HEADER_USER_AGENT, "Android $SDK_INT")
            //header fields - some APIs require this to prevent scraping, analytics, etc
        }

        return chain.proceed(builder.build())
    }

}