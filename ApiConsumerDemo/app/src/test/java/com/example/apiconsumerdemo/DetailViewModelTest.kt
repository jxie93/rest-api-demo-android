package com.example.apiconsumerdemo

import android.net.Uri
import com.example.apiconsumerdemo.data.ContentRepo
import com.example.apiconsumerdemo.data.ContentRepoImpl
import com.example.apiconsumerdemo.data.DemoContentDto
import com.example.apiconsumerdemo.data.RemoteContentDataSource
import com.example.apiconsumerdemo.domain.DemoContent
import com.example.apiconsumerdemo.ui.main.DetailViewModel
import com.example.apiconsumerdemo.ui.main.ListViewModel
import com.example.apiconsumerdemo.usecases.GetDetailContentUseCase
import com.example.apiconsumerdemo.usecases.GetListContentUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.Before
import org.junit.Rule
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @InjectMockKs
    internal lateinit var detailViewModel: DetailViewModel

    @MockK
    internal lateinit var getDetailContentUseCase: GetDetailContentUseCase

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `check loadContent invokes getDetailContentUseCase with correct data`() = runTest {
        // given

        // when
        coEvery { getDetailContentUseCase.invoke(any()) } returns null
        detailViewModel.loadContent("test_id")

        // then
        coVerify { getDetailContentUseCase.invoke("test_id") }
    }

    @Test
    fun `check loadContent sets isLoading to false on content`() = runTest {
        // given

        // when
        coEvery { getDetailContentUseCase.invoke(any()) } returns null
        detailViewModel.loadContent("test_id")
        var result: Boolean? = null

        // then
        val job = testScope.launch {
            detailViewModel.isLoading.collect {
                result = it
            }
        }

        job.cancelAndJoin()

        assertEquals(result, false)
    }

    @Test
    fun `check loadContent sends data to detailDataFlow`() = runTest {
        // given

        // when
        coEvery { getDetailContentUseCase.invoke(any()) } returns null
        detailViewModel.loadContent("test_id")
        var result: DemoContent? = null

        // then
        val job = testScope.launch {
            detailViewModel.detailDataFlow.collect {
                result = it
            }
        }

        job.cancelAndJoin()

        assertEquals(result, null)
    }

}