package com.kirson.googlebooks.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
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