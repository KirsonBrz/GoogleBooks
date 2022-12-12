package com.kirson.googlebooks.screens.explorer

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kirson.googlebooks.HomeModel
import com.kirson.googlebooks.core.base.BaseViewModel
import com.kirson.googlebooks.entity.BookDomainModel
import com.kirson.googlebooks.entity.BooksDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExplorerScreenViewModel @Inject constructor(
    private val homeModel: HomeModel,
) : BaseViewModel<ExplorerScreenViewModel>() {


    private var _uiState = mutableStateOf<ExplorerScreenUIState>(ExplorerScreenUIState.Initial)
    val uiState: State<ExplorerScreenUIState>
        get() = _uiState


    private var _pageFlow = mutableStateOf<Flow<PagingData<BookDomainModel>>>(flowOf())
    val pageFlow: State<Flow<PagingData<BookDomainModel>>>
        get() = _pageFlow


    init {
        viewModelScope.launch(IO) {

            observeData()
        }
    }


    fun observeData() {
        viewModelScope.launch(IO) {

            val searchQuery = homeModel.searchQuery

            searchQuery.flowOn(IO).onStart {
                withContext(Dispatchers.Main) {
                    _uiState.value = ExplorerScreenUIState.Loading(
                        State().copy(
                            refreshInProgress = true
                        )
                    )
                }
            }.flowOn(IO).onCompletion {
                _uiState.value = ExplorerScreenUIState.Loaded(
                    State().copy(
                        refreshInProgress = false,
                    )
                )

            }.flowOn(IO).catch {
                _uiState.value = ExplorerScreenUIState.Error(
                    State().copy(
                        message = "$it"
                    )
                )

            }.flowOn(IO).collect { query ->
                _uiState.value = ExplorerScreenUIState.Loaded(
                    State().copy(
                        searchQuery = TextFieldValue(query),
                        refreshInProgress = false
                    )
                )

            }

            withContext(IO) {
                _pageFlow.value = Pager(PagingConfig(pageSize = 10)) {
                    BooksDataSource(homeModel)
                }.flow.cachedIn(viewModelScope)
            }


        }
    }

    fun selectBookForDetails(book: BookDomainModel) {

        viewModelScope.launch(IO) {
            homeModel.selectBookForDetails(book)
        }


    }


    fun setQuery(searchQuery: String) {

        viewModelScope.launch(IO) {
            homeModel.setQuery(searchQuery)

            _pageFlow.value = Pager(PagingConfig(pageSize = 10)) {
                BooksDataSource(homeModel)
            }.flow.cachedIn(viewModelScope)
        }


    }


}