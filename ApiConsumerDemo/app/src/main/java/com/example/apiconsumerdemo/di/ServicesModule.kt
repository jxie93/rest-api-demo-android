package com.example.apiconsumerdemo.di

import com.example.apiconsumerdemo.data.ContentRepo
import com.example.apiconsumerdemo.data.ContentRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ServicesModule {

    @Binds
    @Singleton
    abstract fun bindContentRepo(impl: ContentRepoImpl): ContentRepo

}