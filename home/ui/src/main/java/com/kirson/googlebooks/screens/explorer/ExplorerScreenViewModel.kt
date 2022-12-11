package com.kirson.googlebooks.screens.explorer

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.kirson.googlebooks.HomeModel
import com.kirson.googlebooks.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
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

    private val _uiStateFlow = MutableStateFlow(State())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    init {
        viewModelScope.launch(IO) {

            observeData()
        }
    }


    fun observeData() {
        viewModelScope.launch(IO) {

            val books = homeModel.books
            val searchQuery = homeModel.searchQuery

            val combineFlow = combine(books, searchQuery, ::Pair)

            combineFlow.flowOn(IO).onStart {
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

            }.flowOn(IO).collect { combineData ->
                _uiState.value = ExplorerScreenUIState.Loaded(
                    State().copy(
                        books = combineData.first.books,
                        searchQuery = TextFieldValue(combineData.second),
                        refreshInProgress = false
                    )
                )

            }


        }
    }

    fun selectBookForDetails(bookTitle: String) {

        viewModelScope.launch(IO) {
            homeModel.selectBookForDetails(bookTitle)
        }


    }


    fun dismissSelectCategory() {
        _uiStateFlow.update {
            it.copy(
                showCategorySelector = false
            )
        }
    }


    fun applySelectCategory(category: String) {
        viewModelScope.launch {
            _uiStateFlow.update { state ->
                state.copy(
                    showCategorySelector = false
                )
            }

        }
        //mainModel.applyCategory(category)

    }

    fun loadBooks(searchQuery: String) {

        viewModelScope.launch(IO) {
            homeModel.getBooks(searchQuery)
        }


    }


}