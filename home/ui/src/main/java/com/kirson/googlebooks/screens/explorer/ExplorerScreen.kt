package com.kirson.googlebooks.screens.explorer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.kirson.googlebooks.components.BookItem
import com.kirson.googlebooks.components.ConnectivityStatus
import com.kirson.googlebooks.components.EmptyContentMessage
import com.kirson.googlebooks.components.ScrollableAppBar
import com.kirson.googlebooks.components.SuggestionScreen
import com.kirson.googlebooks.components.SwipeRefresh
import com.kirson.googlebooks.components.rememberSearchState
import com.kirson.googlebooks.core.utils.ConnectionState
import com.kirson.googlebooks.core.utils.SearchState
import com.kirson.googlebooks.core.utils.connectivityState
import com.kirson.googlebooks.entity.BookDomainModel
import com.kirson.googlebooks.home.ui.R
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun ExplorerScreen(
    viewModel: ExplorerScreenViewModel, navigateToDetails: () -> Unit
) {


    val uiState by viewModel.uiState
    val pageFlow by viewModel.pageFlow




    ExplorerContent(
        booksData = pageFlow.collectAsLazyPagingItems(),
        uiState = uiState,
        onRefresh = {
            viewModel.observeData()
        },
        getBooksWithQuery = { query ->
            if (query.isNotBlank()) {
                viewModel.setQuery(query)
            }
        },
        onBookDetails = { book ->
            viewModel.selectBookForDetails(book)
            navigateToDetails()

        },
        onSelectCategory = { selectedCategory ->
            viewModel.setQuery("+subject:$selectedCategory")

        },


        )
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun ExplorerContent(
    booksData: LazyPagingItems<BookDomainModel>,
    uiState: ExplorerScreenUIState,
    onRefresh: () -> Unit,
    onBookDetails: (BookDomainModel) -> Unit,
    onSelectCategory: (String) -> Unit,
    getBooksWithQuery: (String) -> Unit,
) {

    val connection by connectivityState()
    val isConnected = connection == ConnectionState.Available


    val showBar by remember { mutableStateOf(true) }

    val scrollState = rememberLazyListState()


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
                isConnected = isConnected,
                showBar = showBar,
                onSelectCategory = onSelectCategory,
                getBooksWithQuery = getBooksWithQuery,
                searchState = rememberSearchState(query = uiState.state.searchQuery)
            ) {

                ContentStateReady(
                    booksData = booksData,
                    scrollState = scrollState,
                    onBookDetails = onBookDetails,
                    onRefresh = { onRefresh() },

                    )

            }

        }

        is ExplorerScreenUIState.Loading -> {
            ScreenSlot(
                isConnected = isConnected,
                getBooksWithQuery = getBooksWithQuery,
                onSelectCategory = onSelectCategory,
            ) {
                ContentLoadingState()
            }


        }
    }
}

@Composable
private fun ContentStateReady(

    scrollState: LazyListState,
    onRefresh: () -> Unit,
    booksData: LazyPagingItems<BookDomainModel>,
    onBookDetails: (BookDomainModel) -> Unit,

    ) {
    ContentExplorer(
        onRefresh = onRefresh,
        booksData = booksData,
        onBookDetails = onBookDetails,
        scrollState = scrollState
    )

}


@Composable
private fun ContentExplorer(
    scrollState: LazyListState,
    booksData: LazyPagingItems<BookDomainModel>,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    onBookDetails: (BookDomainModel) -> Unit,
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

                    items(booksData) { book ->
                        book?.let {
                            BookItem(book = book, onBookDetails = onBookDetails)
                        }
                    }
                    when (booksData.loadState.append) {
                        is LoadState.NotLoading -> Unit
                        LoadState.Loading -> {
                            item { LoadingItem() }
                        }

                        is LoadState.Error -> {
                            item {
                                ErrorItem(
                                    message = (booksData.loadState.append as LoadState.Error).error.message.toString()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(42.dp)
                .height(42.dp)
                .padding(8.dp),
            strokeWidth = 5.dp
        )

    }
}

@Composable
fun ErrorItem(message: String) {
    Card(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(8.dp)
        ) {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .width(42.dp)
                    .height(42.dp),
                imageVector = Icons.Filled.Warning,
                contentDescription = "",
            )
            Text(
                color = Color.White,
                text = message,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(CenterVertically)
            )
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ScreenSlot(

    showBar: Boolean = true,
    isConnected: Boolean,
    onSelectCategory: (String) -> Unit,
    getBooksWithQuery: (String) -> Unit,
    searchState: SearchState = rememberSearchState(),
    content: @Composable () -> Unit,

    ) {
    Column(
        modifier = Modifier.statusBarsPadding()
    ) {


        ConnectivityStatus(isConnected = isConnected, onBackOnline = { })

        ScrollableAppBar(
            scrollUpState = showBar,
            getBooksWithQuery = getBooksWithQuery,
            searchState = searchState,
        )

        val tabsList = listOf(
            "Categories",
            "Authors"
        )

        val categoryList = listOf(
            "Adventure",
            "Classic",
            "Drama",
            "Fairytale",
            "Thriller",
            "Fantasy",
            "Mystery",
            "Dictionary",
            "Music",
            "Science",
            "Travel",
        )


        val pagerState = rememberPagerState()

        AnimatedVisibility(
            visible = searchState.query.text.isBlank()

        ) {
            SuggestionScreen(
                tabs = tabsList,
                categories = categoryList,
                searchState = searchState,
                pagerState = pagerState,
                onSelectCategory = onSelectCategory
            )

        }


        AnimatedVisibility(
            visible = searchState.query.text.isNotBlank(),
            enter = fadeIn(animationSpec = tween(900)),
            exit = fadeOut()
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

