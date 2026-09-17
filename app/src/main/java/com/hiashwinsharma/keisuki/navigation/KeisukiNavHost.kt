package com.hiashwinsharma.keisuki.navigation

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hiashwinsharma.keisuki.KeisukiApp
import com.hiashwinsharma.keisuki.feature.focus.navigation.focusScreen
import com.hiashwinsharma.keisuki.feature.focus.navigation.navigateToFocus
import com.hiashwinsharma.keisuki.feature.history.navigation.historyScreen
import com.hiashwinsharma.keisuki.feature.history.navigation.navigateToHistory
import com.hiashwinsharma.keisuki.feature.home.navigation.HOME_ROUTE
import com.hiashwinsharma.keisuki.feature.home.navigation.homeScreen
import com.hiashwinsharma.keisuki.feature.settings.navigation.navigateToSettings
import com.hiashwinsharma.keisuki.feature.settings.navigation.settingsScreen

@Composable
fun KeisukiNavHost(
    navController: NavHostController,
    app: KeisukiApp,
    onSetKeyHandler: (((Int) -> Boolean)?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
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
        homeScreen(
            app = app,
            onCounterClick = { counterId ->
                navController.navigateToFocus(counterId)
            },
            onSettingsClick = {
                navController.navigateToSettings()
            },
            onHistoryClick = {
                navController.navigateToHistory("all")
            }
        )

        settingsScreen(
            app = app,
            onBack = { navController.popBackStack() }
        )

        focusScreen(
            app = app,
            onBack = { navController.popBackStack() },
            onHistoryClick = { counterId ->
                navController.navigateToHistory(counterId)
            },
            onSetKeyHandler = onSetKeyHandler
        )

        historyScreen(
            app = app,
            onBack = { navController.popBackStack() }
        )
    }
}
