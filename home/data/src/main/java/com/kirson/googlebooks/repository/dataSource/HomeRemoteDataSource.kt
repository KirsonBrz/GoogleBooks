package com.kirson.googlebooks.repository.dataSource

import com.kirson.googlebooks.entity.BooksListNetworkModel
import retrofit2.Response

interface HomeRemoteDataSource {

    suspend fun getBooks(searchQuery: String): Response<BooksListNetworkModel>


}