package com.kirson.googlebooks.di

import com.kirson.googlebooks.HomeModel
import com.kirson.googlebooks.HomeModelImpl
import com.kirson.googlebooks.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Singleton
    @Provides
    fun provideMainModel(homeRepository: HomeRepository): HomeModel =
        HomeModelImpl(homeRepository)

}