package com.example.apiconsumerdemo.data

import android.util.Log
import com.example.apiconsumerdemo.domain.DemoContent
import javax.inject.Inject

internal interface ContentRepo {
    suspend fun loadListContent(): List<DemoContent>
}

internal class ContentRepoImpl @Inject constructor(
    private val remoteDatasource: RemoteContentDataSource,
): ContentRepo {

    override suspend fun loadListContent(): List<DemoContent> {
        return try {
            val dtos = remoteDatasource.getListContentDtos()
            dtos.map { DemoContent(it) }
        } catch (e: Exception) {
            Log.e(this.toString(), ContentError.NoContentFound(e).toString())
            emptyList()
        }
    }
}