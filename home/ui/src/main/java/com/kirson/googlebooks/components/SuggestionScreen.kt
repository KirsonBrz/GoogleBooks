package com.kirson.googlebooks.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.kirson.googlebooks.core.utils.SearchState
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SuggestionScreen(
    tabs: List<String>,
    categories: List<String>,
    searchState: SearchState,
    pagerState: PagerState,
    onSelectCategory: (String) -> Unit

) {
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(vertical = 30.dp, horizontal = 16.dp)) {

        Surface(
            shape = RoundedCornerShape(6.dp),
            color = GoogleBooksTheme.colors.contendPrimary,
            elevation = (-15).dp
        ) {


            TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = Color.Transparent,
                contentColor = Color.Transparent,
                indicator = { tabPositions ->

                    Spacer(
                        modifier = Modifier
                            .pagerTabIndicatorOffset(pagerState, tabPositions)
                            .size(tabPositions[0].width)
                            .border(
                                2.dp, GoogleBooksTheme.colors.contendAccentTertiary,
                                RoundedCornerShape(6.dp)
                            )
                    )

                }) {
                tabs.forEachIndexed { index, tab ->

                    Tab(
                        selectedContentColor = GoogleBooksTheme.colors.contendAccentTertiary,
                        unselectedContentColor = GoogleBooksTheme.colors.contendAccentTertiary,
                        text = {
                            Text(
                                text = tab,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W700,
                                textAlign = TextAlign.Center,
                            )
                        },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                    )


                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))

        HorizontalPager(
            count = tabs.size,
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> SearchGrid(
                    categoryList = categories,
                    searchState = searchState,
                    onSelectCategory = onSelectCategory
                )

                1 -> Column(modifier = Modifier.fillMaxSize()) {

                }
//                HistoryList(
//                    history = listOf("Adventure")
//                )
            }
        }
    }
}