package com.kirson.googlebooks.screens.explorer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.IconButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kirson.googlebooks.components.ConnectivityStatus
import com.kirson.googlebooks.components.EmptyContentMessage
import com.kirson.googlebooks.components.MainModalBottomSheetScaffold
import com.kirson.googlebooks.components.SwipeRefresh
import com.kirson.googlebooks.components.TopAppBar
import com.kirson.googlebooks.home.ui.R
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme
import com.kirson.googlebooks.utils.ConnectionState
import com.kirson.googlebooks.utils.connectivityState

@Composable
fun ExplorerScreen(
    viewModel: ExplorerScreenViewModel,
    onPhoneDetails: () -> Unit
) {


    val uiStateFlow by viewModel.uiStateFlow.collectAsState()
    val uiState by viewModel.uiState

    MainContent(
        uiState = uiState, uiStateFlow = uiStateFlow,
        onRefresh = {
            viewModel.observeData()
        },
        applySelectCategory = { selectedCategory ->
            viewModel.applySelectCategory(selectedCategory)
        },
        dismissSelectCategory = {
            viewModel.dismissSelectCategory()
        },
        changeCategory = {
            viewModel.changeCategory()
        },
        onBackOnline = {
            //viewModel.getData()
        }


    )
}

@Composable
private fun MainContent(
    uiState: ExplorerScreenUIState,
    uiStateFlow: State,
    onRefresh: () -> Unit,
    applySelectCategory: (String) -> Unit,
    dismissSelectCategory: () -> Unit,
    changeCategory: () -> Unit,
    onBackOnline: () -> Unit,
) {
    val connection by connectivityState()

    val isConnected = connection == ConnectionState.Available
    when (uiState) {
        is ExplorerScreenUIState.Error -> {
            uiState.state.message?.let { error ->
                EmptyContentMessage(
                    imgRes = R.drawable.img_status_disclaimer_170,
                    title = "Error",
                    description = error,
                )
            }
        }

        ExplorerScreenUIState.Initial -> {
            ContentLoadingState()
        }

        is ExplorerScreenUIState.Loaded -> {
            ScreenSlot(
                changeCategory = changeCategory,
                isConnected = isConnected,
                onBackOnline = onBackOnline
            ) {
                if (false
                //uiState.state.bestSellersPhones != null && uiState.state.homeStorePhones != null
                ) {
                    ContentStateReady(
                        state = uiStateFlow,
                        onRefresh = { onRefresh() },
                        applySelectCategory = applySelectCategory,
                        dismissSelectCategory = dismissSelectCategory
                    )
                } else {
                    EmptyContentMessage(
                        imgRes = R.drawable.img_status_disclaimer_170,
                        title = "Categories",
                        description = "No data :(",
                    )
                }
            }
        }

        is ExplorerScreenUIState.Loading -> {
            ScreenSlot(
                changeCategory = changeCategory,
                isConnected = isConnected,
                onBackOnline = onBackOnline
            ) {
                ContentLoadingState()
            }
        }
    }
}

@Composable
private fun ContentStateReady(
    state: State,
    onRefresh: () -> Unit,
    applySelectCategory: (String) -> Unit,
    dismissSelectCategory: () -> Unit,
) {
    MainModalBottomSheetScaffold(
        state = state,
        content = {
            ContentMain(
                state = state,
                onRefresh = onRefresh,
            )
        },
        applySelectCategory = applySelectCategory,
        dismissSelectCategory = dismissSelectCategory,
    )
}


@Composable
private fun ContentMain(
    state: State,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,

    ) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        SwipeRefresh(
            isRefreshing = false,
            onRefresh = onRefresh,
        ) {
            Column {


            }


        }
    }
}


@Composable
private fun ScreenSlot(
    changeCategory: () -> Unit,
    isConnected: Boolean,
    onBackOnline: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.statusBarsPadding()
    ) {


        TopAppBar(
            leftContent = {},
            centerContent = {


            },
            rightContent = {
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically { fullHeight -> fullHeight },
                    exit = slideOutVertically { fullHeight -> fullHeight },
                ) {
                    IconButton(
                        modifier = Modifier.padding(horizontal = 26.dp), onClick = changeCategory
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter_24),
                            contentDescription = "filter Icon",
                            tint = GoogleBooksTheme.colors.secondaryColor
                        )
                    }
                }

            }
        )
        ConnectivityStatus(isConnected = isConnected, onBackOnline = onBackOnline)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}


@Composable
private fun ContentLoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        CircularProgressIndicator(
            color = GoogleBooksTheme.colors.contendAccentTertiary
        )
    }
}

