package com.example.apiconsumerdemo

import android.net.Uri
import com.example.apiconsumerdemo.data.ContentRepo
import com.example.apiconsumerdemo.data.ContentRepoImpl
import com.example.apiconsumerdemo.data.DemoContentDto
import com.example.apiconsumerdemo.data.RemoteContentDataSource
import com.example.apiconsumerdemo.domain.DemoContent
import com.example.apiconsumerdemo.ui.main.ListUiState
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.Before
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {

    @InjectMockKs
    internal lateinit var listViewModel: ListViewModel

    @MockK
    internal lateinit var getListContentUseCase: GetListContentUseCase

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `check reloadData invokes getListContentUseCase`() = runTest {
        // given

        // when
        coEvery { getListContentUseCase.invoke() } returns emptyList()
        listViewModel.reloadData()

        // then
        coVerify { getListContentUseCase.invoke() }
    }

    @Test
    fun `check reloadData returns valid content to uiState`() = runTest {
        // given

        // when
        coEvery { getListContentUseCase.invoke() } returns emptyList()
        listViewModel.reloadData()
        var result: ListUiState? = null

        // then
        val job = testScope.launch {
            listViewModel.uiState.collect {
                result = it
            }
        }

        job.cancelAndJoin()

        assertEquals(result, ListUiState.Content(emptyList()))
    }

}