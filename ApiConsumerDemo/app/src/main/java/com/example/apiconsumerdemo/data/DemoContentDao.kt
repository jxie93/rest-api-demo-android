package com.example.apiconsumerdemo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.apiconsumerdemo.domain.DemoContent

@Dao
internal interface DemoContentDao {
    @Query("SELECT * FROM demoContent")
    fun getAll(): List<DemoContent>

    @Insert(onConflict = REPLACE)
    fun insertAndReplace(coffee: DemoContent)

    @Insert(onConflict = REPLACE)
    fun insertAndReplaceAll(coffee: List<DemoContent>)

    @Delete
    fun delete(coffee: DemoContent)

    @Query("DELETE FROM demoContent")
    fun deleteAll()
}