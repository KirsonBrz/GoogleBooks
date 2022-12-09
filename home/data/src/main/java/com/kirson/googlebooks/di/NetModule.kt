package com.kirson.googlebooks.di

import com.kirson.googlebooks.api.BooksAPIService
import com.kirson.googlebooks.home.data.BuildConfig
import com.kirson.googlebooks.repository.dataSource.HomeRemoteDataSource
import com.kirson.googlebooks.repository.dataSourceImpl.HomeRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetModule {

    private const val HTTP_CONNECT_TIMEOUT = 60_000L

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {

        val okhttp = OkHttpClient.Builder()
            .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BASIC)
            })
            .build()

        return Retrofit.Builder()
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()

    }

    @Singleton
    @Provides
    fun provideHomeAPIService(retrofit: Retrofit): BooksAPIService =
        retrofit.create(BooksAPIService::class.java)

    @Singleton
    @Provides
    fun provideHomeRemoteDataSource(
        booksAPIService: BooksAPIService
    ): HomeRemoteDataSource = HomeRemoteDataSourceImpl(booksAPIService)


}

