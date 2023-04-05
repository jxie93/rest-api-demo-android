package com.example.apiconsumerdemo.data

import kotlinx.serialization.Serializable

@Serializable
internal data class DemoContentDto(
    val id: String,
    val title: String,
    val description: String,
    val image: String,
)