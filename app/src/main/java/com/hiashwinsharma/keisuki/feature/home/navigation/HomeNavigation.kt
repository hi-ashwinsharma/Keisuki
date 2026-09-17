package com.hiashwinsharma.keisuki.feature.home.navigation

import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hiashwinsharma.keisuki.KeisukiApp
import com.hiashwinsharma.keisuki.feature.home.HomeRoute
import com.hiashwinsharma.keisuki.feature.home.HomeViewModel

const val HOME_ROUTE = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HOME_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(
    app: KeisukiApp,
    onCounterClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    composable(route = HOME_ROUTE) {
        val homeViewModel: HomeViewModel = viewModel {
            HomeViewModel(
                counterRepository = app.counterRepository,
                authRepository = app.authRepository,
                preferencesRepository = app.userPreferencesRepository
            )
        }
        HomeRoute(
            viewModel = homeViewModel,
            onCounterClick = onCounterClick,
            onSettingsClick = onSettingsClick,
            modifier = modifier
        )
    }
}
