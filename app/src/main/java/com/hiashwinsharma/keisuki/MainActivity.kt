package com.hiashwinsharma.keisuki

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.hiashwinsharma.keisuki.core.designsystem.KeisukiTheme
import com.hiashwinsharma.keisuki.core.designsystem.LocalHapticIntensity
import com.hiashwinsharma.keisuki.navigation.KeisukiNavHost

class MainActivity : ComponentActivity() {

    private var volumeKeyHandler: ((Int) -> Boolean)? = null

    fun setVolumeKeyHandler(handler: ((Int) -> Boolean)?) {
        this.volumeKeyHandler = handler
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as KeisukiApp

        setContent {
            val prefs by app.userPreferencesRepository.preferences.collectAsStateWithLifecycle()

            KeisukiTheme(
                themeMode = prefs.themeMode,
                dynamicColor = prefs.isDynamicColor,
                cornerRadius = prefs.cornerRadiusDp.dp
            ) {
                CompositionLocalProvider(LocalHapticIntensity provides prefs.hapticIntensity) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        KeisukiNavHost(
                            navController = navController,
                            app = app,
                            onSetKeyHandler = { handler -> setVolumeKeyHandler(handler) },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            val handled = volumeKeyHandler?.invoke(keyCode) ?: false
            if (handled) return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
