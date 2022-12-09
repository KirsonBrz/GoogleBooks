package com.kirson.googlebooks.api

import com.kirson.googlebooks.entity.BooksListNetworkModel
import com.kirson.googlebooks.home.data.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface BooksAPIService {

    @GET("books/v1/volumes")
    suspend fun getBooks(
        @Query("q")
        searchQuery: String,
        @Query("key")
        apiKey: String = BuildConfig.API_KEY

    ): Response<BooksListNetworkModel>

}