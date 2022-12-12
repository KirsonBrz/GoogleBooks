package com.kirson.googlebooks

import com.kirson.googlebooks.entity.BookDomainModel
import com.kirson.googlebooks.entity.BooksListDomainModel
import kotlinx.coroutines.flow.Flow


interface HomeModel {

    val selectedBook: Flow<BookDomainModel>
    val searchQuery: Flow<String>
    val imageId: Flow<Int>

    suspend fun loadBooksByIndex(index: Int): BooksListDomainModel?
    suspend fun setQuery(searchQuery: String, imageId: Int)
    suspend fun selectBookForDetails(book: BookDomainModel)

}