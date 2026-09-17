package com.hiashwinsharma.keisuki.feature.focus.navigation

import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hiashwinsharma.keisuki.KeisukiApp
import com.hiashwinsharma.keisuki.feature.focus.FocusRoute
import com.hiashwinsharma.keisuki.feature.focus.FocusViewModel

const val FOCUS_ROUTE_PATTERN = "focus/{counterId}"

fun NavController.navigateToFocus(counterId: String, navOptions: NavOptions? = null) {
    navigate("focus/$counterId", navOptions)
}

fun NavGraphBuilder.focusScreen(
    app: KeisukiApp,
    onBack: () -> Unit,
    onHistoryClick: (String) -> Unit = {},
    onSetKeyHandler: (((Int) -> Boolean)?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    composable(
        route = FOCUS_ROUTE_PATTERN,
        arguments = listOf(navArgument("counterId") { type = NavType.StringType })
    ) { backStackEntry ->
        val counterId = backStackEntry.arguments?.getString("counterId") ?: return@composable
        val focusViewModel: FocusViewModel = viewModel(key = counterId) {
            FocusViewModel(
                counterId = counterId,
                counterRepository = app.counterRepository,
                preferencesRepository = app.userPreferencesRepository
            )
        }
        FocusRoute(
            viewModel = focusViewModel,
            onBack = onBack,
            onHistoryClick = onHistoryClick,
            onSetKeyHandler = onSetKeyHandler,
            modifier = modifier
        )
    }
}
