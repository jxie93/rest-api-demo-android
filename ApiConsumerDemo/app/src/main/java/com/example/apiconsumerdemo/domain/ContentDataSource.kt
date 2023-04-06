package com.example.apiconsumerdemo.domain

internal interface ContentDataSource {

    suspend fun fetchListContent(): List<DemoContent>

}