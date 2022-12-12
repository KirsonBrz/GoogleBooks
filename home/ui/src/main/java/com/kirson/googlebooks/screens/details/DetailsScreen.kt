package com.kirson.googlebooks.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kirson.googlebooks.components.BookDetails
import com.kirson.googlebooks.components.ConnectivityStatus
import com.kirson.googlebooks.components.EmptyContentMessage
import com.kirson.googlebooks.core.utils.ConnectionState
import com.kirson.googlebooks.core.utils.connectivityState
import com.kirson.googlebooks.entity.BookDomainModel
import com.kirson.googlebooks.home.ui.R
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Composable
fun DetailsScreen(
    viewModel: DetailsScreenViewModel, onBackScreen: () -> Unit
) {


    val uiState by viewModel.uiState



    DetailsContent(
        uiState = uiState,
        onBackScreen = { onBackScreen() },
    )


}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun DetailsContent(
    uiState: DetailsScreenUIState,
    onBackScreen: () -> Unit,
) {
    val connection by connectivityState()

    val isConnected = connection == ConnectionState.Available


    when (uiState) {
        is DetailsScreenUIState.Error -> {
            uiState.state.message?.let { error ->
                EmptyContentMessage(
                    imgRes = R.drawable.img_status_disclaimer_170,
                    title = "Ошибка",
                    description = error,
                )
            }
        }

        DetailsScreenUIState.Initial -> {
            ContentLoadingState()
        }

        is DetailsScreenUIState.Loaded -> {
            ScreenSlot(
                isConnected = isConnected,
            ) {
                if (uiState.state.book != null) {
                    ContentDetailsReady(
                        book = uiState.state.book, onBackScreen = onBackScreen
                    )
                } else {
                    EmptyContentMessage(
                        imgRes = R.drawable.img_status_disclaimer_170,
                        title = "Phone Details",
                        description = "No data :(",
                    )
                }
            }
        }

        is DetailsScreenUIState.Loading -> {
            ScreenSlot(
                isConnected = isConnected,
            ) {
                ContentLoadingState()
            }
        }
    }
}


@Composable
private fun ContentDetailsReady(
    modifier: Modifier = Modifier,
    book: BookDomainModel,
    onBackScreen: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Column {

            BookDetails(book = book, upPress = onBackScreen)


        }


    }
}


@Composable
private fun ScreenSlot(
    isConnected: Boolean, content: @Composable () -> Unit

) {
    Column(
        modifier = Modifier.statusBarsPadding()
    ) {

        ConnectivityStatus(isConnected = isConnected, onBackOnline = { })
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