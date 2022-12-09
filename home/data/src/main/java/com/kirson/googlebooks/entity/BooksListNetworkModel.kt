package com.kirson.googlebooks.entity

import com.google.gson.annotations.SerializedName

data class BooksListNetworkModel(

    @SerializedName("items")
    val books: List<BookNetworkModel>?,

    val kind: String,

    val totalItems: Int
)