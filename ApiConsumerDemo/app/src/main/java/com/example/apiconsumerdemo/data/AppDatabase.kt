package com.example.apiconsumerdemo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.apiconsumerdemo.domain.DemoContent
import com.example.apiconsumerdemo.domain.EntityConverters

@Database(entities = [DemoContent::class], version = 1)
@TypeConverters(EntityConverters::class)
internal abstract class AppDatabase: RoomDatabase() {
    abstract fun demoContentDao(): DemoContentDao
}