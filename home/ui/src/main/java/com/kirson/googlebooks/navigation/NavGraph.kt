package com.kirson.googlebooks.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kirson.googlebooks.screens.details.DetailsScreen
import com.kirson.googlebooks.screens.details.DetailsScreenViewModel
import com.kirson.googlebooks.screens.explorer.ExplorerScreen
import com.kirson.googlebooks.screens.explorer.ExplorerScreenViewModel

fun NavGraphBuilder.addHomeGraph(
    popBackStack: () -> Unit,
    onPhoneDetails: () -> Unit
) {
    navigation(
        startDestination = NavTarget.Explorer.route,
        route = NavTarget.RootModule.route
    ) {
        composable(NavTarget.Explorer.route) {
            val viewModel: ExplorerScreenViewModel = hiltViewModel()
            ExplorerScreen(viewModel, onPhoneDetails = onPhoneDetails)
        }
        composable(NavTarget.Details.route) {
            val viewModel: DetailsScreenViewModel = hiltViewModel()
            DetailsScreen(viewModel = viewModel, onBackScreen = popBackStack)
        }

    }


}