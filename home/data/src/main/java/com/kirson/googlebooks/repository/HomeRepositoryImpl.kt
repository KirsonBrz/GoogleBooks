package com.kirson.googlebooks.repository


import android.util.Log
import com.kirson.googlebooks.HomeRepository
import com.kirson.googlebooks.entity.BooksListNetworkModel

import com.kirson.googlebooks.repository.dataSource.HomeRemoteDataSource
import javax.inject.Inject


class HomeRepositoryImpl @Inject constructor(
    private val homeRemoteDataSource: HomeRemoteDataSource,
    //private val homeLocalDataSource: HomeLocalDataSource
) : HomeRepository {


    override suspend fun getBooks(searchQuery: String): BooksListNetworkModel? {
        var books: BooksListNetworkModel? = null

        try {
            val response = homeRemoteDataSource.getBooks(searchQuery)
            val data = response.body()
            if (data != null) {
                books = data
            }
        } catch (exception: Exception) {
            Log.i("MyTag", exception.message.toString())
        }


        return books

    }


}


