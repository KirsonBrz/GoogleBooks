package com.kirson.googlebooks.entity

data class BooksListDomainModel(

    val books: List<BookDomainModel>?,
    val kind: String,
    val totalItems: Int
)