package com.hiashwinsharma.keisuki.feature.history.navigation

import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hiashwinsharma.keisuki.KeisukiApp
import com.hiashwinsharma.keisuki.feature.history.HistoryRoute
import com.hiashwinsharma.keisuki.feature.history.HistoryViewModel

const val HISTORY_ROUTE_PATTERN = "history/{counterId}"

fun NavController.navigateToHistory(counterId: String, navOptions: NavOptions? = null) {
    navigate("history/$counterId", navOptions)
}

fun NavGraphBuilder.historyScreen(
    app: KeisukiApp,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    composable(
        route = HISTORY_ROUTE_PATTERN,
        arguments = listOf(navArgument("counterId") { type = NavType.StringType })
    ) { backStackEntry ->
        val counterId = backStackEntry.arguments?.getString("counterId") ?: "all"
        val viewModel: HistoryViewModel = viewModel(key = "history_$counterId") {
            HistoryViewModel(
                initialCounterId = counterId,
                counterRepository = app.counterRepository
            )
        }
        HistoryRoute(
            viewModel = viewModel,
            onBack = onBack,
            modifier = modifier
        )
    }
}
