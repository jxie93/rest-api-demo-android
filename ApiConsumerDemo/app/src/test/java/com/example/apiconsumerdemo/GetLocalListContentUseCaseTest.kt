package com.example.apiconsumerdemo

import com.example.apiconsumerdemo.data.ContentRepo
import com.example.apiconsumerdemo.usecases.GetLocalListContentUseCase
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
class GetLocalListContentUseCaseTest {

    @InjectMockKs
    internal lateinit var getLocalListContentUseCase: GetLocalListContentUseCase

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
        coEvery { contentRepo.loadLocalListContent() } returns emptyList()
        getLocalListContentUseCase.invoke()

        // then
        coVerify { contentRepo.loadLocalListContent() }
    }

}