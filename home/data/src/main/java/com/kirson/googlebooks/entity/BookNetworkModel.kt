package com.kirson.googlebooks.entity

import com.google.gson.annotations.SerializedName

data class BookNetworkModel(
    val id: String,
    @SerializedName("volumeInfo")
    val bookInfoNetworkModel: BookInfoNetworkModel
)