package com.example.apiconsumerdemo.usecases

import com.example.apiconsumerdemo.data.ContentRepo
import com.example.apiconsumerdemo.domain.DemoContent
import javax.inject.Inject

internal class GetLocalListContentUseCase @Inject constructor(
    private val contentRepo: ContentRepo
) {
    suspend operator fun invoke(): List<DemoContent> {
        return contentRepo.loadLocalListContent()
    }
}