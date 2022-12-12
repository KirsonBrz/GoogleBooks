package com.kirson.googlebooks.core.utils

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue


@Stable
class SearchState(
    query: TextFieldValue,
    imageId: Int,
    focused: Boolean,
    searching: Boolean,

    ) {
    var query by mutableStateOf(query)
    var imageId by mutableStateOf(imageId)
    var focused by mutableStateOf(focused)
    var searching by mutableStateOf(searching)

}