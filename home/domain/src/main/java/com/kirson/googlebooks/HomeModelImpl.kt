package com.kirson.googlebooks

import com.kirson.googlebooks.core.entity.mapDistinctNotNullChanges
import com.kirson.googlebooks.core.utils.swapToCategoryPrefix
import com.kirson.googlebooks.entity.BookDomainModel
import com.kirson.googlebooks.entity.BooksListDomainModel
import com.kirson.googlebooks.mappers.toDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class HomeModelImpl @Inject constructor(
    private val repository: HomeRepository,
) : HomeModel {

    private val stateFlow = MutableStateFlow(State())

    data class State(
        val searchQuery: String = "",
        val selectedBook: BookDomainModel? = null,
    )


    override val selectedBook: Flow<BookDomainModel>
        get() = stateFlow.mapDistinctNotNullChanges {
            it.selectedBook
        }.flowOn(Dispatchers.IO)
    override val searchQuery: Flow<String>
        get() = stateFlow.mapDistinctNotNullChanges {
            it.searchQuery
        }.flowOn(Dispatchers.IO)

    override suspend fun loadBooksByIndex(index: Int): BooksListDomainModel? {

        var books: BooksListDomainModel? = null

        val data = repository.getBooks(stateFlow.value.searchQuery.swapToCategoryPrefix(), index)

        if (data != null) {
            books = data.toDomainModel()
        }

        return books
    }


    override suspend fun setQuery(searchQuery: String) {

        stateFlow.update { state ->
            state.copy(
                searchQuery = searchQuery.swapToCategoryPrefix()
            )
        }


    }


    override suspend fun selectBookForDetails(book: BookDomainModel) {

        stateFlow.update { state ->
            state.copy(
                selectedBook = book
            )
        }

    }


}