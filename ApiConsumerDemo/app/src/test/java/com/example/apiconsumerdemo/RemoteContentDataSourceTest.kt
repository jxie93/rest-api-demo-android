package com.example.apiconsumerdemo

import com.example.apiconsumerdemo.data.DemoContentDto
import com.example.apiconsumerdemo.data.RemoteContentDataSource
import com.example.apiconsumerdemo.domain.DemoContent
import com.example.apiconsumerdemo.remote.api.ApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.Before
import retrofit2.HttpException
import retrofit2.Response
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteContentDataSourceTest {

    @InjectMockKs
    internal lateinit var remoteDataSource: RemoteContentDataSource

    @MockK
    internal lateinit var apiService: ApiService

    private val testDto
        get() = DemoContentDto(
            id = "test_id",
            title = "test_title",
            description = "test_description",
            image = "test_url"
        )

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `check fetchListContentDtos invokes apiService`() = runTest {
        // given

        // when
        coEvery { apiService.getListContent() } returns Response.success(emptyList())
        remoteDataSource.fetchListContentDtos()

        // then
        coVerify { apiService.getListContent() }
    }

    @Test
    fun `check fetchListContentDtos returns correct data if api call successful`() = runTest {
        // given

        // when
        coEvery { apiService.getListContent() } returns Response.success(listOf(testDto))
        val result = remoteDataSource.fetchListContentDtos()

        // then
        assertEquals(result, listOf(testDto))
    }

    @Test
    fun `check fetchListContentDtos throws http exception if api call fails`() = runTest {
        // given

        // when
        val responseBody = ResponseBody.create(MediaType.parse("application/json")!!, "error message")
        val httpException = HttpException(Response.error<Any>(404, responseBody))
        coEvery { apiService.getListContent() } throws httpException

        // then
        assertFailsWith<HttpException> {
            remoteDataSource.fetchListContentDtos()
        }
    }
}