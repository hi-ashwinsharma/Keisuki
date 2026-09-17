package com.hiashwinsharma.keisuki.ui.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hiashwinsharma.keisuki.KeisukiApp
import com.hiashwinsharma.keisuki.ui.screens.focus.CounterFocusScreen
import com.hiashwinsharma.keisuki.ui.screens.focus.CounterFocusViewModel
import com.hiashwinsharma.keisuki.ui.screens.home.CounterGridScreen
import com.hiashwinsharma.keisuki.ui.screens.home.CounterGridViewModel
import com.hiashwinsharma.keisuki.ui.screens.settings.SettingsScreen
import com.hiashwinsharma.keisuki.ui.screens.settings.SettingsViewModel

sealed class Screen(val route: String) {
    data object Grid : Screen("grid")
    data object Settings : Screen("settings")
    data object Focus : Screen("focus/{counterId}") {
        fun createRoute(counterId: String) = "focus/$counterId"
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    app: KeisukiApp,
    onSetKeyHandler: (((Int) -> Boolean)?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Grid.route,
        enterTransition = {
            fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing)) +
            scaleIn(initialScale = 0.92f, animationSpec = tween(280, easing = FastOutSlowInEasing))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing)) +
            scaleOut(targetScale = 1.05f, animationSpec = tween(200, easing = FastOutSlowInEasing))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing)) +
            scaleIn(initialScale = 1.05f, animationSpec = tween(280, easing = FastOutSlowInEasing))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing)) +
            scaleOut(targetScale = 0.92f, animationSpec = tween(200, easing = FastOutSlowInEasing))
        },
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        composable(route = Screen.Grid.route) {
            val gridViewModel: CounterGridViewModel = viewModel {
                CounterGridViewModel(
                    counterRepository = app.counterRepository,
                    authRepository = app.authRepository,
                    preferencesRepository = app.userPreferencesRepository
                )
            }
            CounterGridScreen(
                viewModel = gridViewModel,
                onCounterClick = { counterId ->
                    navController.navigate(Screen.Focus.createRoute(counterId))
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(route = Screen.Settings.route) {
            val settingsViewModel: SettingsViewModel = viewModel {
                SettingsViewModel(
                    preferencesRepository = app.userPreferencesRepository,
                    counterRepository = app.counterRepository,
                    authRepository = app.authRepository
                )
            }
            SettingsScreen(
                viewModel = settingsViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Focus.route,
            arguments = listOf(navArgument("counterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val counterId = backStackEntry.arguments?.getString("counterId") ?: return@composable
            val focusViewModel: CounterFocusViewModel = viewModel(key = counterId) {
                CounterFocusViewModel(
                    counterId = counterId,
                    counterRepository = app.counterRepository,
                    preferencesRepository = app.userPreferencesRepository
                )
            }
            CounterFocusScreen(
                viewModel = focusViewModel,
                onSetKeyHandler = onSetKeyHandler,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
