package com.example.apiconsumerdemo.domain

import android.net.Uri
import com.example.apiconsumerdemo.data.DemoContentDto
import java.util.UUID

internal data class DemoContent(
    val id: String,
    val title: String,
    val description: String,
    val image: Uri,
    val isPlaceholder: Boolean
) {
    companion object {
        fun getPlaceholders(count: Int) = List(count) {
            DemoContent(
                id = UUID.randomUUID().toString(),
                title = "",
                description = "",
                image = "".uriFromString,
                isPlaceholder = true
            )
        }
    }

    constructor(dto: DemoContentDto) : this(
        id = dto.id,
        title = dto.title,
        description = dto.description,
        image = dto.image.uriFromString,
        isPlaceholder = false
    )
}

val String?.uriFromString: Uri
    get() = try {
        Uri.parse(this)
    } catch (ex: Exception) {
        Uri.EMPTY
    }