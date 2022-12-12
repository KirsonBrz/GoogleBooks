package com.kirson.googlebooks

import NavigationComponent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.kirson.googlebooks.navigation.InitialScreen
import com.kirson.googlebooks.navigation.Navigator
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoogleBooksTheme {

                var showInitialScreen by rememberSaveable { mutableStateOf(true) }
                if (showInitialScreen) {
                    Surface(
                        color = GoogleBooksTheme.colors.backgroundSecondary,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        InitialScreen(onTimeout = { showInitialScreen = false })
                    }
                } else {
                    MainScreenContent()
                }
            }
        }
    }
}


@Composable
fun MainScreenContent() {


    val navController = rememberNavController()
    val navigator = Navigator()




    Scaffold(
        backgroundColor = GoogleBooksTheme.colors.backgroundPrimary,
        topBar = {

        },
        bottomBar = {

        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
            )
            {
                NavigationComponent(navController, navigator)
            }
        }
    )
}



