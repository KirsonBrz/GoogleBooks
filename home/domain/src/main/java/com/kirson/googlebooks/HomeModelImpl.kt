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
        val books: BooksListDomainModel? = null,
        val searchQuery: String? = null,
        val selectedBookTitle: String? = null,
    )

    override val books: Flow<BooksListDomainModel>
        get() = stateFlow.mapDistinctNotNullChanges {
            it.books
        }.flowOn(Dispatchers.IO)
    override val selectedBook: Flow<BookDomainModel>
        get() = stateFlow.mapDistinctNotNullChanges {
            it.books?.books?.find { book ->
                book.title == it.selectedBookTitle
            }
        }.flowOn(Dispatchers.IO)
    override val searchQuery: Flow<String>
        get() = stateFlow.mapDistinctNotNullChanges {
            it.searchQuery
        }.flowOn(Dispatchers.IO)


    override suspend fun getBooks(searchQuery: String): BooksListDomainModel? {
        var books: BooksListDomainModel? = null

        val data = repository.getBooks(searchQuery)

        if (data != null) {
            books = data.toDomainModel()
        }

        stateFlow.update { state ->
            state.copy(
                books = books,
                searchQuery = searchQuery.swapToCategoryPrefix()
            )
        }

        return books
    }

    override suspend fun selectBookForDetails(bookTitle: String) {

        stateFlow.update { state ->
            state.copy(
                selectedBookTitle = bookTitle
            )
        }

    }


}