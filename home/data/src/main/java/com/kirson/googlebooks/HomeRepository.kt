package com.kirson.googlebooks

import com.kirson.googlebooks.entity.BooksListNetworkModel


interface HomeRepository {
    suspend fun getBooks(searchQuery: String): BooksListNetworkModel?


}