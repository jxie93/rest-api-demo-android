package com.example.apiconsumerdemo.data

import com.example.apiconsumerdemo.domain.ContentDataSource
import com.example.apiconsumerdemo.remote.api.ApiService
import retrofit2.HttpException
import javax.inject.Inject

internal class RemoteContentDataSource @Inject constructor(
    private val apiService: ApiService
): ContentDataSource {
    override suspend fun getListContentDtos(): List<DemoContentDto> {
        val result = apiService.getListContent()
        if (result.isSuccessful) {
            return result.body() ?: emptyList()
        } else {
            throw HttpException(result)
        }
    }
}