package com.kirson.googlebooks.screens.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kirson.googlebooks.components.ConnectivityStatus
import com.kirson.googlebooks.components.EmptyContentMessage
import com.kirson.googlebooks.components.TopAppBar
import com.kirson.googlebooks.home.ui.R
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme
import com.kirson.googlebooks.utils.ConnectionState
import com.kirson.googlebooks.utils.connectivityState


@Composable
fun DetailsScreen(
    viewModel: DetailsScreenViewModel,
    onBackScreen: () -> Unit
) {

    val uiStateFlow by viewModel.uiStateFlow.collectAsState()
    val uiState by viewModel.uiState

    DetailsContent(
        uiState = uiState,
        uiStateFlow = uiStateFlow,
        onBackScreen = { onBackScreen() },
        onBackOnline = {
            //viewModel.getData()
        }
    )


}

@Composable
private fun DetailsContent(
    uiState: DetailsScreenUIState,
    uiStateFlow: State,
    onBackScreen: () -> Unit,
    onBackOnline: () -> Unit
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
                onBackOnline = onBackOnline,
                onBackScreen = onBackScreen
            ) {
                if (false
                    //uiState.state.phoneDetails != null
                ) {
                    ContentDetailsReady(
                        state = uiStateFlow,
                        //phoneDetails = uiState.state.phoneDetails,

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
                onBackScreen = onBackScreen,
                onBackOnline = onBackOnline
            ) {
                ContentLoadingState()
            }
        }
    }
}


@Composable
private fun ContentDetailsReady(
    state: State,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Column {



        }


    }
}



@Composable
private fun ScreenSlot(
    isConnected: Boolean,
    onBackScreen: () -> Unit,
    onBackOnline: () -> Unit,
    content: @Composable () -> Unit

) {
    Column(
        modifier = Modifier.statusBarsPadding()
    ) {


        TopAppBar(leftContent = {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically { fullHeight -> fullHeight },
                exit = slideOutVertically { fullHeight -> fullHeight },
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(horizontal = 26.dp)
                        .background(
                            color = GoogleBooksTheme.colors.secondaryColor,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    onClick = onBackScreen
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "back",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )

                }
            }


        }, centerContent = {



        }, rightContent = {


        })
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

@Preview
@Composable
fun DetailBoxPreview() {
    GoogleBooksTheme {


    }
}
