package com.example.apiconsumerdemo.remote.api

import com.example.apiconsumerdemo.data.DemoContentDto
import retrofit2.Response
import retrofit2.http.GET

internal interface ApiService {
    @GET("/coffee/hot")
    suspend fun getListContent(): Response<List<DemoContentDto>>
}