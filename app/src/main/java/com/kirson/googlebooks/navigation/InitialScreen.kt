package com.kirson.googlebooks.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme
import kotlinx.coroutines.delay

@Composable
fun InitialScreen(modifier: Modifier = Modifier, onTimeout: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = GoogleBooksTheme.colors.primaryColor),
        contentAlignment = Alignment.Center
    )
    {

        val currentOnTimeout by rememberUpdatedState(onTimeout)



        Surface(
            color = GoogleBooksTheme.colors.contendPrimary,
            modifier = Modifier
                .size(280.dp)
                .clip(CircleShape)

        ) {


        }
        Text(
            text = "Google\nBooks",
            fontSize = 52.sp,
            fontWeight = FontWeight.W800,
            color = GoogleBooksTheme.colors.contendAccentTertiary,
            textAlign = TextAlign.Center,


            )



        LaunchedEffect(true) {
            delay(1300)
            currentOnTimeout()
        }

    }
}