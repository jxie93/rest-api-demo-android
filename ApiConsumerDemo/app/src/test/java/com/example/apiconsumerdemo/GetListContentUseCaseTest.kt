package com.example.apiconsumerdemo

import android.net.Uri
import com.example.apiconsumerdemo.data.ContentRepo
import com.example.apiconsumerdemo.data.ContentRepoImpl
import com.example.apiconsumerdemo.data.DemoContentDto
import com.example.apiconsumerdemo.data.RemoteContentDataSource
import com.example.apiconsumerdemo.domain.DemoContent
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.Before
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class GetListContentUseCaseTest {

    @InjectMockKs
    internal lateinit var getListContentUseCase: GetListContentUseCase

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
        coEvery { contentRepo.loadListContent() } returns emptyList()
        getListContentUseCase.invoke()

        // then
        coVerify { contentRepo.loadListContent() }
    }

}