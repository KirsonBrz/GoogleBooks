package com.kirson.googlebooks

import com.kirson.googlebooks.entity.BookDomainModel
import com.kirson.googlebooks.entity.BooksListDomainModel
import kotlinx.coroutines.flow.Flow


interface HomeModel {

    val books: Flow<BooksListDomainModel>
    val selectedBook: Flow<BookDomainModel>
    val searchQuery: Flow<String>

    suspend fun getBooks(searchQuery: String): BooksListDomainModel?
    suspend fun selectBookForDetails(bookTitle: String)

}