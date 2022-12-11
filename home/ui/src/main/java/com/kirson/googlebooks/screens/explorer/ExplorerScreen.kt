package com.kirson.googlebooks.screens.explorer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
import com.kirson.googlebooks.utils.toHttpsPrefix
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun ExplorerScreen(
    viewModel: ExplorerScreenViewModel, navigateToDetails: () -> Unit
) {

    val uiStateFlow by viewModel.uiStateFlow.collectAsState()
    val uiState by viewModel.uiState

    MainContent(uiState = uiState, uiStateFlow = uiStateFlow, onRefresh = {
        viewModel.observeData()
    }, applySelectCategory = { selectedCategory ->
        viewModel.applySelectCategory(selectedCategory)
    }, dismissSelectCategory = {
        viewModel.dismissSelectCategory()
    }, getBooksWithQuery = { query ->
        if (query.isNotBlank()) {
            viewModel.loadBooks(query)
        }

    }, onBookDetails = { bookTitle ->
        viewModel.selectBookForDetails(bookTitle)
        navigateToDetails()

    },
        onSelectCategory = { selectedCategory ->
            viewModel.loadBooks("+subject:$selectedCategory")

        }


    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun MainContent(
    uiState: ExplorerScreenUIState,
    uiStateFlow: State,
    onRefresh: () -> Unit,
    applySelectCategory: (String) -> Unit,
    dismissSelectCategory: () -> Unit,
    onBookDetails: (String) -> Unit,
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
            ) {
                if (uiState.state.books != null) {
                    ContentStateReady(
                        state = uiStateFlow,
                        books = uiState.state.books,
                        scrollState = scrollState,
                        onBookDetails = onBookDetails,
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
    state: State,
    scrollState: LazyListState,
    onRefresh: () -> Unit,
    books: List<BookDomainModel>,
    onBookDetails: (String) -> Unit,
    applySelectCategory: (String) -> Unit,
    dismissSelectCategory: () -> Unit
) {
    MainModalBottomSheetScaffold(
        state = state,
        content = {
            ContentMain(
                onRefresh = onRefresh,
                books = books,
                onBookDetails = onBookDetails,
                scrollState = scrollState
            )
        },
        applySelectCategory = applySelectCategory,
        dismissSelectCategory = dismissSelectCategory,
    )
}


@Composable
private fun ContentMain(
    scrollState: LazyListState,
    books: List<BookDomainModel>,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    onBookDetails: (String) -> Unit,
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

                        BookItem(
                            book = it, onBookDetails = onBookDetails
                        )


                    }

                }


            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(
    book: BookDomainModel, onBookDetails: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(166.dp)
            .padding(vertical = 3.dp),
        onClick = {
            onBookDetails(book.title)
        },
        colors = CardDefaults.cardColors(containerColor = GoogleBooksTheme.colors.contendPrimary),
        elevation = CardDefaults.elevatedCardElevation(),
        shape = CardDefaults.elevatedShape


    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),

            ) {

            if (book.largeImage != null) {

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(book.largeImage!!.toHttpsPrefix()).crossfade(true).build(),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(width = 100.dp, height = 150.dp)


                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    tint = GoogleBooksTheme.colors.contendAccentTertiary,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(width = 100.dp, height = 150.dp)
                        .background(color = Color.Transparent, shape = CircleShape)
                )

            }
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = book.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = GoogleBooksTheme.colors.contendAccentTertiary
            )

        }
    }
}


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

        val categoryList = listOf(
            "Adventure",
            "Children’s",
            "Classic",
            "Drama",
            "Fairytale",
            "Thriller",
            "Fantasy",
            "Mystery",
            "Dictionary",
            "Philosophy",
            "Music",
            "Science",
            "Satire",
            "Travel",
            "Foreign Language Study",
        )

        val listState = rememberLazyGridState()

        AnimatedVisibility(
            visible = searchState.query.text.isBlank()

        ) {

            LazyVerticalGrid(
                state = listState,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)

            ) {

                items(items = categoryList) { categoryName ->
                    TextButton(
                        onClick = {
                            searchState.query = TextFieldValue(categoryName)
                            onSelectCategory(categoryName)
                        },
                        modifier = Modifier
                            .size(120.dp)
                            .background(
                                color = GoogleBooksTheme.colors.contendPrimary,
                                shape = RoundedCornerShape(12.dp)
                            )
                    )
                    {
                        Text(
                            text = categoryName,
                            fontSize = 16.sp,
                            color = GoogleBooksTheme.colors.contendAccentTertiary
                        )

                    }

                }
            }


        }




        AnimatedVisibility(
            visible = searchState.query.text.isNotBlank(),
            enter = fadeIn(),
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScrollableAppBar(
    modifier: Modifier = Modifier,
    background: Color = GoogleBooksTheme.colors.backgroundPrimary,
    scrollUpState: Boolean,
    searchState: SearchState,
    getBooksWithQuery: (String) -> Unit,
) {

    AnimatedVisibility(
        visible = scrollUpState, enter = expandVertically(), exit = shrinkVertically()
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
            Row {


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
                    if (searchState.focused) {
                        getBooksWithQuery(searchState.query.text)
                    }
                    searchState.searching = false
                }
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

