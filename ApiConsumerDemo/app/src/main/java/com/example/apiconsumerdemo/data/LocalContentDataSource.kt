package com.example.apiconsumerdemo.data

import com.example.apiconsumerdemo.domain.ContentDataSource
import com.example.apiconsumerdemo.domain.DemoContent
import javax.inject.Inject

internal class LocalContentDataSource @Inject constructor(
    private val dao: DemoContentDao
): ContentDataSource {
    override suspend fun fetchListContent(): List<DemoContent> {
        return dao.getAll()
    }

    fun saveContent(content: DemoContent) {
        dao.insertAndReplace(content)
    }

    fun saveContent(contentList: List<DemoContent>) {
        dao.insertAndReplaceAll(contentList)
    }

    fun deleteContent(content: DemoContent) {
        dao.delete(content)
    }

    fun deleteAll() {
        dao.deleteAll()
    }
}