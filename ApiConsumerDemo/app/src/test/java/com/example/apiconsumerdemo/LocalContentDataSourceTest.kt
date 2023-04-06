package com.example.apiconsumerdemo

import android.net.Uri
import com.example.apiconsumerdemo.data.DemoContentDao
import com.example.apiconsumerdemo.data.DemoContentDto
import com.example.apiconsumerdemo.data.LocalContentDataSource
import com.example.apiconsumerdemo.domain.DemoContent
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LocalContentDataSourceTest {

    @InjectMockKs
    internal lateinit var localContentDataSource: LocalContentDataSource

    @MockK
    internal lateinit var dao: DemoContentDao

    private val testDto
        get() = DemoContentDto(
            id = "test_id",
            title = "test_title",
            description = "test_description",
            image = "test_url"
        )

    @Before
    fun setUp() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `check fetchListContent invokes dao`() = runTest {
        // given

        // when
        coEvery { dao.getAll() } returns emptyList()
        localContentDataSource.fetchListContent()

        // then
        coVerify { dao.getAll() }
    }

    @Test
    fun `check saveContent invokes dao`() = runTest {
        // given
        val content = DemoContent(testDto)

        // when
        localContentDataSource.saveContent(content)
        localContentDataSource.saveContent(listOf(content))

        // then
        coVerify { dao.insertAndReplace(content) }
        coVerify { dao.insertAndReplaceAll(listOf(content)) }
    }

    @Test
    fun `check deleteContent invokes dao`() = runTest {
        // given
        val content = DemoContent(testDto)

        // when
        localContentDataSource.deleteContent(content)

        // then
        coVerify { dao.delete(content) }
    }

    @Test
    fun `check deleteAll invokes dao`() = runTest {
        // given

        // when
        localContentDataSource.deleteAll()

        // then
        coVerify { dao.deleteAll() }
    }

}