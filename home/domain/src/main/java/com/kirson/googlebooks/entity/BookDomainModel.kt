package com.kirson.googlebooks.entity

data class BookDomainModel(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val smallImage: String?,
    val largeImage: String?,
    val pageCount: Int?,
    val publishedDate: String?,
)
