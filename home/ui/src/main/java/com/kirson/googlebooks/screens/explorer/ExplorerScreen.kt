package com.kirson.googlebooks.screens.explorer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.kirson.googlebooks.components.BookItem
import com.kirson.googlebooks.components.CategoryItem
import com.kirson.googlebooks.components.ConnectivityStatus
import com.kirson.googlebooks.components.EmptyContentMessage
import com.kirson.googlebooks.components.ProgressIndicator
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
import kotlinx.coroutines.launch

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
                viewModel.setQuery(query, 0)
            }
        },
        onBookDetails = { book ->
            viewModel.selectBookForDetails(book)
            navigateToDetails()

        },
        onSelectCategory = { selectedCategory, imageId ->
            viewModel.setQuery("+subject:$selectedCategory", imageId)

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
    onSelectCategory: (String, Int) -> Unit,
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
                searchState = rememberSearchState(
                    query = uiState.state.searchQuery,
                    imageId = uiState.state.imageId
                )
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
                                    scrollState = scrollState
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
            .wrapContentHeight(), contentAlignment = Alignment.Center
    ) {

        ProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
        )

    }
}

@Composable
fun ErrorItem(scrollState: LazyListState) {

    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = GoogleBooksTheme.colors.contendPrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(0)
                    }
                }) {
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp),
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "",
                    tint = GoogleBooksTheme.colors.contendAccentTertiary
                )
            }

        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ScreenSlot(

    showBar: Boolean = true,
    isConnected: Boolean,
    onSelectCategory: (String, Int) -> Unit,
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
            "Categories", "Authors"
        )

        val categoryList = listOf(
            CategoryItem.Adventure,
            CategoryItem.Biography,
            CategoryItem.Business,
            CategoryItem.Classic,
            CategoryItem.Detective,
            CategoryItem.Drama,
            CategoryItem.Fairytale,
            CategoryItem.Fantasy,
            CategoryItem.Folklore,
            CategoryItem.Historical,
            CategoryItem.Horror,
            CategoryItem.Humor,
            CategoryItem.Legend,
            CategoryItem.Mystery,
            CategoryItem.Mythology,
            CategoryItem.NonFiction,
            CategoryItem.Play,
            CategoryItem.Poetry,
            CategoryItem.Romance,
            CategoryItem.ScienceFiction,

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

