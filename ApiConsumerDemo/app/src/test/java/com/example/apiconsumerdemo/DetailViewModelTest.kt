package com.example.apiconsumerdemo

import android.net.Uri
import com.example.apiconsumerdemo.domain.DemoContent
import com.example.apiconsumerdemo.ui.main.DetailUiState
import com.example.apiconsumerdemo.ui.main.DetailViewModel
import com.example.apiconsumerdemo.usecases.GetLocalDetailContentUseCase
import com.example.apiconsumerdemo.usecases.GetRemoteDetailContentUseCase
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @InjectMockKs
    internal lateinit var detailViewModel: DetailViewModel

    @MockK
    internal lateinit var getRemoteDetailContentUseCase: GetRemoteDetailContentUseCase

    @MockK
    internal lateinit var getLocalDetailContentUseCase: GetLocalDetailContentUseCase

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `check loadContent invokes getDetailContentUseCase with correct data`() = runTest {
        // given

        // when
        coEvery { getRemoteDetailContentUseCase.invoke(any()) } returns null
        detailViewModel.loadContent("test_id")

        // then
        coVerify { getRemoteDetailContentUseCase.invoke("test_id") }
    }

    @Test
    fun `check loadContent returns valid content to uiState`() = runTest {
        // given
        val testContent = DemoContent(
            id = "test_id",
            title = "test_title",
            description = "test_description",
            image = Uri.parse(""),
            isPlaceholder = false
        )

        // when
        coEvery { getRemoteDetailContentUseCase.invoke(any()) } returns testContent
        detailViewModel.loadContent("test_id")
        var result: DetailUiState? = null

        // then
        val job = testScope.launch {
            detailViewModel.uiState.collect {
                result = it
            }
        }

        job.cancelAndJoin()

        assertEquals(result, DetailUiState.Content(testContent))
    }

    @Test
    fun `check loadContent returns error uiState if no valid content`() = runTest {
        // given

        // when
        coEvery { getRemoteDetailContentUseCase.invoke(any()) } returns null
        detailViewModel.loadContent("test_id")
        var result: DetailUiState? = null

        // then
        val job = testScope.launch {
            detailViewModel.uiState.collect {
                result = it
            }
        }

        job.cancelAndJoin()

        assertTrue(result is DetailUiState.Error)
    }

    @Test
    fun `check localLocalContent invokes getLocalDetailContentUseCase with correct data`() = runTest {
        // given

        // when
        coEvery { getLocalDetailContentUseCase.invoke(any()) } returns null
        detailViewModel.localLocalContent("test_id")

        // then
        coVerify { getLocalDetailContentUseCase.invoke("test_id") }
    }

}