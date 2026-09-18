package com.hiashwinsharma.keisuki.feature.focus.components

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics
import com.hiashwinsharma.keisuki.core.model.Counter
import com.hiashwinsharma.keisuki.data.preferences.UserPreferences
import com.hiashwinsharma.keisuki.feature.focus.FocusUiAction

@Composable
fun FocusBody(
    counter: Counter,
    preferences: UserPreferences,
    cornerRadius: Dp,
    onAction: (FocusUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()
    val colorToken = counter.colorToken
    var totalDragY by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .then(
                if (preferences.swipeGesturesEnabled) {
                    Modifier.pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragStart = { totalDragY = 0f },
                            onDragEnd = { totalDragY = 0f },
                            onDragCancel = { totalDragY = 0f },
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                totalDragY += dragAmount
                                if (totalDragY < -70f) {
                                    haptics.tick()
                                    onAction(FocusUiAction.OnIncrement)
                                    totalDragY = 0f
                                } else if (totalDragY > 70f) {
                                    haptics.tick()
                                    onAction(FocusUiAction.OnDecrement)
                                    totalDragY = 0f
                                }
                            }
                        )
                    }
                } else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. HERO COUNTER DISPLAY
        FocusCounterDisplay(
            count = counter.count,
            colorToken = colorToken,
            cornerRadius = cornerRadius,
            modifier = Modifier.weight(1f)
        )

        // 2. INLINE STEP SELECTOR CHIPS
        FocusStepSelector(
            currentStep = counter.step,
            colorToken = colorToken,
            cornerRadius = cornerRadius,
            onSelectStep = { stepVal ->
                onAction(
                    FocusUiAction.OnUpdateCounter(
                        title = counter.title,
                        count = counter.count,
                        step = stepVal,
                        colorToken = counter.colorToken
                    )
                )
            }
        )

        // 3. ERGONOMIC THUMB CONTROLS
        FocusControlPad(
            colorToken = colorToken,
            isLeftHanded = preferences.isLeftHanded,
            isPillButtons = preferences.isPillButtons,
            rapidHoldSpeed = preferences.rapidHoldSpeed,
            cornerRadius = cornerRadius,
            onIncrement = { onAction(FocusUiAction.OnIncrement) },
            onDecrement = { onAction(FocusUiAction.OnDecrement) }
        )
    }
}
