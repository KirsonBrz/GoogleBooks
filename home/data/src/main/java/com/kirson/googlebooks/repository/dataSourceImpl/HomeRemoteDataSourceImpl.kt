package com.kirson.googlebooks.repository.dataSourceImpl

import com.kirson.googlebooks.api.BooksAPIService
import com.kirson.googlebooks.entity.BooksListNetworkModel
import com.kirson.googlebooks.repository.dataSource.HomeRemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class HomeRemoteDataSourceImpl @Inject constructor(
    private val booksAPIService: BooksAPIService
) : HomeRemoteDataSource {

    override suspend fun getBooks(searchQuery: String): Response<BooksListNetworkModel> =
        booksAPIService.getBooks(searchQuery)


}