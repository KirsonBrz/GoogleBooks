package com.kirson.googlebooks.di

import com.kirson.googlebooks.MainModel
import com.kirson.googlebooks.MainModelImpl
import com.kirson.googlebooks.MainRepository
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
    fun provideMainModel(mainRepository: MainRepository): MainModel =
        MainModelImpl(mainRepository)

}