package com.kirson.googlebooks.di


import com.kirson.googlebooks.HomeRepository
import com.kirson.googlebooks.repository.HomeRepositoryImpl
import com.kirson.googlebooks.repository.dataSource.HomeRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {


    @Singleton
    @Provides
    fun provideHomeRepository(
        homeRemoteDataSource: HomeRemoteDataSource
    ): HomeRepository =
        HomeRepositoryImpl(homeRemoteDataSource)

}

