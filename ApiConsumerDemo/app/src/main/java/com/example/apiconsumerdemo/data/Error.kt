package com.example.apiconsumerdemo.data

sealed class ContentError(msg: String, cause: Exception? = null) : Exception(msg, cause) {

    data class NoContentFound(override val cause: Exception? = null) :
        Error("No content data found at the API", cause)

}