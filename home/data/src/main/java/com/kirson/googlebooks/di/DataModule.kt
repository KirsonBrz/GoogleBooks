package com.kirson.googlebooks.di


import com.kirson.googlebooks.MainRepository
import com.kirson.googlebooks.api.BooksAPIService
import com.kirson.googlebooks.home.data.BuildConfig
import com.kirson.googlebooks.repository.MainRepositoryImpl
import com.kirson.googlebooks.repository.dataSource.MainRemoteDataSource
import com.kirson.googlebooks.repository.dataSourceImpl.MainRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {


    @Singleton
    @Provides
    fun provideMainRepository(
        mainRemoteDataSource: MainRemoteDataSource
    ): MainRepository =
        MainRepositoryImpl(mainRemoteDataSource)

}

@Module
@InstallIn(SingletonComponent::class)
object NetModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()


    @Singleton
    @Provides
    fun provideMainAPIService(retrofit: Retrofit): BooksAPIService =
        retrofit.create(BooksAPIService::class.java)

    @Singleton
    @Provides
    fun provideMainRemoteDataSource(
        booksAPIService: BooksAPIService
    ): MainRemoteDataSource = MainRemoteDataSourceImpl(booksAPIService)


}