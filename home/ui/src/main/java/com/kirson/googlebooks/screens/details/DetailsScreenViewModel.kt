package com.kirson.googlebooks.screens.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.kirson.googlebooks.HomeModel
import com.kirson.googlebooks.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val homeModel: HomeModel
) : BaseViewModel<DetailsScreenViewModel>() {

    private var _uiState = mutableStateOf<DetailsScreenUIState>(DetailsScreenUIState.Initial)
    val uiState: State<DetailsScreenUIState>
        get() = _uiState

    private val _uiStateFlow = MutableStateFlow(State())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            observeData()
        }
    }



    private fun observeData() = viewModelScope.launch(Dispatchers.IO) {

        val book = homeModel.selectedBook



        book.flowOn(Dispatchers.IO).onStart {
            withContext(Dispatchers.Main) {
                _uiState.value = DetailsScreenUIState.Loading(
                    State().copy(
                        refreshInProgress = true
                    )
                )
            }
        }.flowOn(Dispatchers.IO).onCompletion {
            _uiState.value = DetailsScreenUIState.Loaded(
                State().copy(
                    refreshInProgress = false,
                )
            )

        }.flowOn(Dispatchers.IO).catch {
            _uiState.value = DetailsScreenUIState.Error(
                State().copy(
                    message = "$it"
                )
            )

        }.flowOn(Dispatchers.IO).collect { selectedBook ->
            _uiState.value = DetailsScreenUIState.Loaded(
                State().copy(
                    book = selectedBook,
                    refreshInProgress = false
                )
            )

        }


    }


}