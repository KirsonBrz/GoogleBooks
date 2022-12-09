package com.kirson.googlebooks

import com.kirson.googlebooks.entity.BooksListDomainModel
import kotlinx.coroutines.flow.Flow


interface HomeModel {

    val books: Flow<BooksListDomainModel>

    suspend fun getBooks(searchQuery: String): BooksListDomainModel?


}