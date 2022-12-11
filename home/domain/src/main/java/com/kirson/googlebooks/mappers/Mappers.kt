package com.kirson.googlebooks.mappers

import com.kirson.googlebooks.entity.BookDomainModel
import com.kirson.googlebooks.entity.BookNetworkModel
import com.kirson.googlebooks.entity.BooksListDomainModel
import com.kirson.googlebooks.entity.BooksListNetworkModel


fun BooksListNetworkModel.toDomainModel(): BooksListDomainModel = BooksListDomainModel(
    books = this.books?.toDomainBooks(),
    kind = this.kind,
    totalItems = this.totalItems
)


fun List<BookNetworkModel>.toDomainBooks(): List<BookDomainModel> =
    this.map {
        BookDomainModel(
            title = it.bookInfoNetworkModel.title,
            authors = it.bookInfoNetworkModel.authors,
            description = it.bookInfoNetworkModel.description,
            smallImage = it.bookInfoNetworkModel.imageLinks?.smallThumbnail,
            largeImage = it.bookInfoNetworkModel.imageLinks?.thumbnail,
            pageCount = it.bookInfoNetworkModel.pageCount,
            publishedDate = it.bookInfoNetworkModel.publishedDate,
        )
    }

