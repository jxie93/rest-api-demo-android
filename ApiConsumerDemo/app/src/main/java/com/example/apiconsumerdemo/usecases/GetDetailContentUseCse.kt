package com.example.apiconsumerdemo.usecases

import com.example.apiconsumerdemo.data.ContentRepo
import com.example.apiconsumerdemo.domain.DemoContent
import javax.inject.Inject

internal class GetDetailContentUseCse @Inject constructor(
    private val contentRepo: ContentRepo
) {
    suspend operator fun invoke(itemId: String): DemoContent? {
        return contentRepo.loadListContent().firstOrNull { it.id == itemId }
    }
}