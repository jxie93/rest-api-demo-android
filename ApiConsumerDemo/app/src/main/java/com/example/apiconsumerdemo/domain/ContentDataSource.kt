package com.example.apiconsumerdemo.domain

import com.example.apiconsumerdemo.data.DemoContentDto

internal interface ContentDataSource {

    suspend fun getListContentDtos(): List<DemoContentDto>

}