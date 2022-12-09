package com.kirson.googlebooks

import com.kirson.googlebooks.core.entity.mapDistinctNotNullChanges
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
        val books: BooksListDomainModel? = null
    )

    override val books: Flow<BooksListDomainModel>
        get() = stateFlow.mapDistinctNotNullChanges {
            it.books
        }.flowOn(Dispatchers.IO)

    override suspend fun getBooks(searchQuery: String): BooksListDomainModel? {
        var books: BooksListDomainModel? = null

        val data = repository.getBooks(searchQuery)

        if (data != null) {
            books = data.toDomainModel()
        }

        stateFlow.update { state ->
            state.copy(
                books = books
            )
        }

        return books
    }


}