package com.hiashwinsharma.keisuki.feature.settings.navigation

import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hiashwinsharma.keisuki.KeisukiApp
import com.hiashwinsharma.keisuki.feature.settings.SettingsRoute
import com.hiashwinsharma.keisuki.feature.settings.SettingsViewModel

const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    navigate(SETTINGS_ROUTE, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    app: KeisukiApp,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    composable(route = SETTINGS_ROUTE) {
        val settingsViewModel: SettingsViewModel = viewModel {
            SettingsViewModel(
                preferencesRepository = app.userPreferencesRepository,
                counterRepository = app.counterRepository,
                authRepository = app.authRepository
            )
        }
        SettingsRoute(
            viewModel = settingsViewModel,
            onBack = onBack,
            modifier = modifier
        )
    }
}
