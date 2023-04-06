package com.example.apiconsumerdemo

import com.example.apiconsumerdemo.data.ContentRepo
import com.example.apiconsumerdemo.usecases.GetLocalDetailContentUseCase
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
class GetLocalDetailContentUseCaseTest {

    @InjectMockKs
    internal lateinit var getLocalDetailContentUseCase: GetLocalDetailContentUseCase

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
        getLocalDetailContentUseCase.invoke("test_id")

        // then
        coVerify { contentRepo.loadLocalListContent() }
    }


}