package com.kirson.googlebooks.screens.explorer

import androidx.compose.runtime.Immutable
import com.kirson.googlebooks.entity.BookDomainModel

sealed class ExplorerScreenUIState {
    object Initial : ExplorerScreenUIState()
    data class Loading(val state: State) : ExplorerScreenUIState()
    data class Loaded(val state: State) : ExplorerScreenUIState()
    data class Error(val state: State) : ExplorerScreenUIState()
}

@Immutable
data class State(
    val refreshInProgress: Boolean = false,
    val message: String? = null,


    val books: List<BookDomainModel>? = null,

    val selectedCategory: String? = null,
    val showCategorySelector: Boolean = false,
)