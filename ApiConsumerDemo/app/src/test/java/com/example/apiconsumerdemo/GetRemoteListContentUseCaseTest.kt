package com.example.apiconsumerdemo

import com.example.apiconsumerdemo.data.ContentRepo
import com.example.apiconsumerdemo.usecases.GetRemoteListContentUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetRemoteListContentUseCaseTest {

    @InjectMockKs
    internal lateinit var getRemoteListContentUseCase: GetRemoteListContentUseCase

    @MockK
    internal lateinit var contentRepo: ContentRepo

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `check invokes contentRepo`() = runTest {
        // given

        // when
        coEvery { contentRepo.loadRemoteListContent() } returns emptyList()
        getRemoteListContentUseCase.invoke()

        // then
        coVerify { contentRepo.loadRemoteListContent() }
    }

}