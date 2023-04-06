package com.example.apiconsumerdemo

import android.net.Uri
import com.example.apiconsumerdemo.data.ContentRepoImpl
import com.example.apiconsumerdemo.data.DemoContentDto
import com.example.apiconsumerdemo.data.LocalContentDataSource
import com.example.apiconsumerdemo.data.RemoteContentDataSource
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
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class ContentRepoTest {

    @InjectMockKs
    internal lateinit var contentRepo: ContentRepoImpl

    @MockK
    internal lateinit var remoteDataSource: RemoteContentDataSource

    @MockK
    internal lateinit var localDataSource: LocalContentDataSource

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
    fun `check loadListContent invokes remoteDatasource`() = runTest {
        // given

        // when
        coEvery { remoteDataSource.fetchListContent() } returns emptyList()
        contentRepo.loadRemoteListContent()

        // then
        coVerify { remoteDataSource.fetchListContent() }
    }

    @Test
    fun `check loadListContent returns mapped data`() = runTest {
        // given

        // when
        coEvery { remoteDataSource.fetchListContent() } returns listOf(DemoContent(testDto))
        val result = contentRepo.loadRemoteListContent()

        // then
        assertEquals(result, listOf(DemoContent(testDto)))
    }

    @Test
    fun `check loadListContent returns no data for http exception`() = runTest {
        // given

        // when
        val errorResponseBody = ResponseBody.create(MediaType.parse("application/json")!!, "error message")
        val httpException = HttpException(Response.error<Any>(404, errorResponseBody))
        coEvery { remoteDataSource.fetchListContent() } throws httpException
        val result = contentRepo.loadRemoteListContent()

        // then
        assertEquals(result, emptyList<DemoContent>())
    }

    @Test
    fun `check loadListContent saves data to local`() = runTest {
        // given
        val data = listOf(DemoContent(testDto))

        // when
        coEvery { remoteDataSource.fetchListContent() } returns data
        val result = contentRepo.loadRemoteListContent()

        // then
        coVerify { localDataSource.saveContent(data) }
    }

    @Test
    fun `check loadLocalListContent invokes localDataSource`() = runTest {
        // given

        // when
        coEvery { localDataSource.fetchListContent() } returns listOf(DemoContent(testDto))
        val result = contentRepo.loadLocalListContent()

        // then
        coVerify { localDataSource.fetchListContent() }
    }

}