package com.example.apiconsumerdemo.domain

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.apiconsumerdemo.data.DemoContentDto
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

@OptIn(ExperimentalSerializationApi::class)
internal class EntityConverters {
    private val json = Json { isLenient = true }
    @TypeConverter
    fun fromStringToList(input: String): List<String> = json.decodeFromString(input)
    @TypeConverter
    fun fromList(list: List<String>): String = json.encodeToString(list)
    @TypeConverter
    fun fromStringToUri(input: String): Uri = input.uriFromString
    @TypeConverter
    fun fromUri(uri: Uri): String = uri.toString()
}

@Entity
internal data class DemoContent(
    @PrimaryKey val id: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("image") val image: Uri,
    @ColumnInfo("isPlaceholder") val isPlaceholder: Boolean
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

    fun toDto(): DemoContentDto = DemoContentDto(
        id = id,
        title = title,
        description = description,
        image = image.toString(),
    )
}

val String?.uriFromString: Uri
    get() = try {
        Uri.parse(this)
    } catch (ex: Exception) {
        Uri.EMPTY
    }