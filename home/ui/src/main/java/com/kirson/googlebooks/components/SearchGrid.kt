package com.kirson.googlebooks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirson.googlebooks.core.utils.SearchState
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme

@Composable
fun SearchGrid(
    listState: LazyGridState,
    categoryList: List<String>,
    searchState: SearchState,
    onSelectCategory: (String) -> Unit

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
                    searchState.focused = false
                    searchState.query = TextFieldValue("Category: $categoryName")
                    onSelectCategory(categoryName)
                }, modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = GoogleBooksTheme.colors.contendPrimary,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Text(
                    text = categoryName,
                    fontSize = 18.sp,
                    color = GoogleBooksTheme.colors.contendAccentTertiary,
                    fontWeight = FontWeight.W600,
                    textAlign = TextAlign.Center
                )

            }

        }
    }


}