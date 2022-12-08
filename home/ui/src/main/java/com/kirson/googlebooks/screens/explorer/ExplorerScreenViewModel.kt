package com.kirson.googlebooks.screens.explorer

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.kirson.googlebooks.MainModel
import com.kirson.googlebooks.core.base.BaseViewModel
import com.kirson.googlebooks.core.entity.SortConfiguration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExplorerScreenViewModel @Inject constructor(
    private val mainModel: MainModel,
) : BaseViewModel<ExplorerScreenViewModel>() {

    private var _uiState = mutableStateOf<ExplorerScreenUIState>(ExplorerScreenUIState.Initial)
    val uiState: State<ExplorerScreenUIState>
        get() = _uiState

    private val _uiStateFlow = MutableStateFlow(State())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    init {
        viewModelScope.launch(IO) {
            //mainModel.getAllPhones()
            observeData()
        }
    }


    fun observeData() {
        viewModelScope.launch(IO) {
//
//            val phones = mainModel.allPhones
//            phones.flowOn(IO).onStart {
//                withContext(Dispatchers.Main) {
//                    _uiState.value = ExplorerScreenUIState.Loading(
//                        State().copy(
//                            refreshInProgress = true
//                        )
//                    )
//                }
//            }.flowOn(IO).onCompletion {
//                _uiState.value = ExplorerScreenUIState.Loaded(
//                    State().copy(
//                        refreshInProgress = false,
//                    )
//                )
//
//            }.flowOn(IO).catch {
//                _uiState.value = ExplorerScreenUIState.Error(
//                    State().copy(
//                        message = "$it"
//                    )
//                )
//
//            }.flowOn(IO).collect { phonesList ->
//                _uiState.value = ExplorerScreenUIState.Loaded(
//                    State().copy(
//                        homeStorePhones = phonesList.homeStoreList,
//                        bestSellersPhones = phonesList.bestSellerList,
//                        refreshInProgress = false
//                    )
//                )
//
//            }

        }
    }


    fun dismissSelectCategory() {
        _uiStateFlow.update {
            it.copy(
                showCategorySelector = false
            )
        }
    }


    fun changeCategory() {
        _uiStateFlow.update {
            it.copy(
                showCategorySelector = true
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


}