package com.kirson.googlebooks.screens.details


import androidx.compose.runtime.Immutable
import com.kirson.googlebooks.entity.BookDomainModel

sealed class DetailsScreenUIState {
    object Initial : DetailsScreenUIState()
    data class Loading(val state: State) : DetailsScreenUIState()
    data class Loaded(val state: State) : DetailsScreenUIState()
    data class Error(val state: State) : DetailsScreenUIState()
}

@Immutable
data class State(
    val refreshInProgress: Boolean = false,
    val message: String? = null,

    val book: BookDomainModel? = null

)