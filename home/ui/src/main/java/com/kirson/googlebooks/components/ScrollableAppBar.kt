package com.kirson.googlebooks.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirson.googlebooks.core.utils.SearchState
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScrollableAppBar(
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
                    delay(650)
                    searchState.searching = false
                }
            }
        }
    }


}

@Composable
fun rememberSearchState(
    query: TextFieldValue = TextFieldValue(""),
    focused: Boolean = false,
    searching: Boolean = false,
): SearchState {
    return remember {
        SearchState(
            query = query, focused = focused, searching = searching
        )
    }
}


@ExperimentalAnimationApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit,
    onBack: () -> Unit,
    searching: Boolean,
    focused: Boolean,
    modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {

        AnimatedVisibility(visible = (focused || query.text.isNotBlank())) {
            // Back button
            IconButton(modifier = Modifier.padding(start = 2.dp), onClick = {
                focusManager.clearFocus()
                keyboardController?.hide()
                onBack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = GoogleBooksTheme.colors.contendAccentTertiary
                )
            }
        }

        SearchTextField(
            query,
            onQueryChange,
            onSearchFocusChange,
            onClearQuery,
            searching,
            focused,
            modifier.weight(1f)
        )
    }
}

@Composable
fun SearchTextField(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit,
    searching: Boolean,
    focused: Boolean,
    modifier: Modifier = Modifier
) {

    val focusRequester = remember { FocusRequester() }

    Surface(
        modifier = modifier.then(
            Modifier
                .height(56.dp)
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = if (!focused && query.text.isBlank()) 16.dp else 0.dp,
                    end = 16.dp
                )
        ),
        color = GoogleBooksTheme.colors.contendPrimary,
        shape = RoundedCornerShape(percent = 50),
    ) {

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Box(
                contentAlignment = Alignment.CenterStart, modifier = modifier
            ) {

                if (!focused && query.text.isEmpty()) {
                    SearchHint(modifier.padding(start = 48.dp, end = 8.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    if (!focused && query.text.isEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = GoogleBooksTheme.colors.contendAccentTertiary,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }

                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .onFocusChanged {
                                onSearchFocusChange(it.isFocused)
                            }
                            .focusRequester(focusRequester)
                            .padding(top = 9.dp, bottom = 8.dp, start = 24.dp, end = 8.dp),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W500,
                            color = GoogleBooksTheme.colors.contendAccentTertiary
                        ),
                        cursorBrush = SolidColor(Color.LightGray),
                    )

                    when {
                        searching -> {
//                            CircularProgressIndicator(
//                                modifier = Modifier
//                                    .padding(10.dp)
//                                    .size(24.dp),
//                                color = GoogleBooksTheme.colors.contendAccentTertiary
//                            )
                            ProgressIndicator(
                                modifier = Modifier
                                    .padding(all = 10.dp)
                                    .weight(0.15f)
                            )
                        }

                        query.text.isNotEmpty() && focused -> {
                            IconButton(onClick = onClearQuery) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = null,
                                    tint = GoogleBooksTheme.colors.contendAccentTertiary
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
private fun SearchHint(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)

    ) {
        Text(
            color = Color(0xff757575),
            text = "Search a Tag or Description",
        )
    }
}