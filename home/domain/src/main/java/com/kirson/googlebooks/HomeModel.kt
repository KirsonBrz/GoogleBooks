package com.kirson.googlebooks

import com.kirson.googlebooks.entity.BookDomainModel
import com.kirson.googlebooks.entity.BooksListDomainModel
import kotlinx.coroutines.flow.Flow


interface HomeModel {

    val selectedBook: Flow<BookDomainModel>
    val searchQuery: Flow<String>

    suspend fun loadBooksByIndex(index: Int): BooksListDomainModel?
    suspend fun setQuery(searchQuery: String)
    suspend fun selectBookForDetails(book: BookDomainModel)

}