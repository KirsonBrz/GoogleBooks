package com.kirson.googlebooks.screens.explorer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.kirson.googlebooks.components.ConnectivityStatus
import com.kirson.googlebooks.components.EmptyContentMessage
import com.kirson.googlebooks.components.MainModalBottomSheetScaffold
import com.kirson.googlebooks.components.SearchBar
import com.kirson.googlebooks.components.SwipeRefresh
import com.kirson.googlebooks.components.rememberSearchState
import com.kirson.googlebooks.entity.BookDomainModel
import com.kirson.googlebooks.home.ui.R
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme
import com.kirson.googlebooks.utils.ConnectionState
import com.kirson.googlebooks.utils.SearchState
import com.kirson.googlebooks.utils.connectivityState

@Composable
fun ExplorerScreen(
    viewModel: ExplorerScreenViewModel,
    onPhoneDetails: () -> Unit
) {

    val uiStateFlow by viewModel.uiStateFlow.collectAsState()
    val uiState by viewModel.uiState

    MainContent(uiState = uiState, uiStateFlow = uiStateFlow, onRefresh = {
        viewModel.observeData()
    }, applySelectCategory = { selectedCategory ->
        viewModel.applySelectCategory(selectedCategory)
    }, dismissSelectCategory = {
        viewModel.dismissSelectCategory()
    }, changeCategory = {
        viewModel.changeCategory()
    }, onBackOnline = {
        //viewModel.getData()
    }, getBooksWithQuery = { query ->
        if (query.isNotBlank()) {
            viewModel.loadBooks(query)
        }

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
    getBooksWithQuery: (String) -> Unit,
) {

    val connection by connectivityState()
    val isConnected = connection == ConnectionState.Available


    var showBar by remember { mutableStateOf(false) }

    val scrollState = rememberLazyListState()

    showBar = scrollState.firstVisibleItemIndex > 0


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
                onBackOnline = onBackOnline,
                showBar = showBar,
                getBooksWithQuery = getBooksWithQuery
            ) {
                if (uiState.state.books != null) {
                    ContentStateReady(
                        state = uiStateFlow,
                        books = uiState.state.books,
                        scrollState = scrollState,
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
                onBackOnline = onBackOnline,
                getBooksWithQuery = getBooksWithQuery
            ) {
                ContentLoadingState()
            }


        }
    }
}

@Composable
private fun ContentStateReady(
    state: State,
    scrollState: LazyListState,
    onRefresh: () -> Unit,
    books: List<BookDomainModel>,
    applySelectCategory: (String) -> Unit,
    dismissSelectCategory: () -> Unit
) {
    MainModalBottomSheetScaffold(
        state = state,
        content = {
            ContentMain(
                state = state, onRefresh = onRefresh, books = books, scrollState = scrollState
            )
        },
        applySelectCategory = applySelectCategory,
        dismissSelectCategory = dismissSelectCategory,
    )
}


@Composable
private fun ContentMain(
    state: State,
    scrollState: LazyListState,
    books: List<BookDomainModel>,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        SwipeRefresh(
            isRefreshing = false,
            onRefresh = onRefresh,
        ) {
            Column {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp),
                    state = scrollState
                ) {

                    items(books) {
                        Text(text = it.title)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = it.description.toString())
                        Spacer(modifier = Modifier.height(15.dp))

                    }

                }


            }


        }
    }
}


@Composable
private fun ScreenSlot(
    changeCategory: () -> Unit,
    showBar: Boolean = true,
    getBooksWithQuery: (String) -> Unit,
    isConnected: Boolean,
    onBackOnline: () -> Unit,
    searchState: SearchState = rememberSearchState(),
    content: @Composable () -> Unit,

    ) {
    Column(
        modifier = Modifier.statusBarsPadding()
    ) {


        ConnectivityStatus(isConnected = isConnected, onBackOnline = onBackOnline)
        ScrollableAppBar(
            scrollUpState = showBar,
            getBooksWithQuery = getBooksWithQuery,
            searchState = searchState
        )

        AnimatedVisibility(
            visible = searchState.query.text.isNotBlank(),
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
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
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScrollableAppBar(
    modifier: Modifier = Modifier,
    background: Color = GoogleBooksTheme.colors.backgroundPrimary,
    scrollUpState: Boolean,
    searchState: SearchState,
    getBooksWithQuery: (String) -> Unit,
) {
    var visibility by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visibility,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = background),
            )
            Row() {
                SearchBar(
                    query = searchState.query,
                    onQueryChange = { searchState.query = it },
                    onSearchFocusChange = { searchState.focused = it },
                    onClearQuery = { searchState.query = TextFieldValue("") },
                    onBack = { searchState.query = TextFieldValue("") },
                    searching = searchState.searching,
                    focused = searchState.focused,
                )


                LaunchedEffect(searchState.query.text) {
                    searchState.searching = true
                    getBooksWithQuery(searchState.query.text)
                    searchState.searching = false
                }
            }
        }
    }

    LaunchedEffect(scrollUpState) {
        visibility = !scrollUpState
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

