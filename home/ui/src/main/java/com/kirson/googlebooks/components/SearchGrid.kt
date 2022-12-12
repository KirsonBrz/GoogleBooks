package com.kirson.googlebooks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirson.googlebooks.core.utils.SearchState
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme

@Composable
fun SearchGrid(
    categoryList: List<String>,
    searchState: SearchState,
    onSelectCategory: (String) -> Unit

) {

    LazyVerticalGrid(
        state = rememberLazyGridState(),
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 15.dp),
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)

    ) {

        items(items = categoryList) { categoryName ->
            Button(
                onClick = {
                    searchState.focused = false
                    searchState.query = TextFieldValue("Category: $categoryName")
                    onSelectCategory(categoryName)
                },
                contentPadding = PaddingValues(3.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = GoogleBooksTheme.colors.contendAccentTertiary,
                        modifier = Modifier
                            .background(
                                GoogleBooksTheme.colors.contendPrimary,
                                shape = CircleShape
                            )
                            .padding(16.dp),
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = categoryName,
                        fontSize = 15.sp,
                        maxLines = 2,
                        color = GoogleBooksTheme.colors.contendAccentTertiary,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
    }


}