package com.example.apiconsumerdemo.data

import android.util.Log
import com.example.apiconsumerdemo.domain.DemoContent
import javax.inject.Inject

internal interface ContentRepo {
    suspend fun loadRemoteListContent(): List<DemoContent>
    suspend fun loadLocalListContent(): List<DemoContent>
}

internal class ContentRepoImpl @Inject constructor(
    private val remoteDatasource: RemoteContentDataSource,
    private val localDataSource: LocalContentDataSource
): ContentRepo {

    override suspend fun loadRemoteListContent(): List<DemoContent> {
        return try {
            val remoteData = remoteDatasource.fetchListContent()
            localDataSource.saveContent(remoteData)
            remoteData
        } catch (e: Exception) {
            Log.e(this.toString(), ContentError.NoContentFound(e).toString())
            emptyList()
        }
    }

    override suspend fun loadLocalListContent(): List<DemoContent> {
        return localDataSource.fetchListContent()
    }

}