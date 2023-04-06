package com.example.apiconsumerdemo

import com.example.apiconsumerdemo.ui.main.ListUiState
import com.example.apiconsumerdemo.ui.main.ListViewModel
import com.example.apiconsumerdemo.usecases.GetLocalListContentUseCase
import com.example.apiconsumerdemo.usecases.GetRemoteListContentUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {

    @InjectMockKs
    internal lateinit var listViewModel: ListViewModel

    @MockK
    internal lateinit var getRemoteListContentUseCase: GetRemoteListContentUseCase

    @MockK
    internal lateinit var getLocalListContentUseCase: GetLocalListContentUseCase

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
        coEvery { getRemoteListContentUseCase.invoke() } returns emptyList()
        listViewModel.reloadData()

        // then
        coVerify { getRemoteListContentUseCase.invoke() }
    }

    @Test
    fun `check reloadData returns valid content to uiState`() = runTest {
        // given

        // when
        coEvery { getRemoteListContentUseCase.invoke() } returns emptyList()
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

    @Test
    fun `check loadLocalData invokes getLocalListContentUseCase`() = runTest {
        // given

        // when
        coEvery { getLocalListContentUseCase.invoke() } returns emptyList()
        listViewModel.loadLocalData()

        // then
        coVerify { getLocalListContentUseCase.invoke() }
    }

}