package com.hiashwinsharma.keisuki.feature.focus.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics
import com.hiashwinsharma.keisuki.core.model.ColorToken
import com.hiashwinsharma.keisuki.data.preferences.RapidHoldSpeed
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FocusControlPad(
    colorToken: ColorToken,
    isLeftHanded: Boolean,
    isPillButtons: Boolean,
    rapidHoldSpeed: RapidHoldSpeed,
    cornerRadius: Dp,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonShape = if (isPillButtons) CircleShape else RoundedCornerShape(cornerRadius * 1.1f)

    val incContainerColor = if (colorToken.isCustomColor) colorToken.primaryColor else MaterialTheme.colorScheme.primary
    val incContentColor = MaterialTheme.colorScheme.onPrimary
    val decContainerColor = if (colorToken.isCustomColor) colorToken.primaryColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceContainerHigh
    val decContentColor = colorToken.onContainerColor

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val decBtn = @Composable { m: Modifier ->
            RepeatableActionButton(
                icon = Icons.Default.Remove,
                iconSize = 34.dp,
                contentDescription = "Decrement",
                containerColor = decContainerColor,
                contentColor = decContentColor,
                shape = buttonShape,
                rapidHoldSpeed = rapidHoldSpeed,
                onTrigger = onDecrement,
                modifier = m
            )
        }

        val incBtn = @Composable { m: Modifier ->
            RepeatableActionButton(
                icon = Icons.Default.Add,
                iconSize = 42.dp,
                contentDescription = "Increment",
                containerColor = incContainerColor,
                contentColor = incContentColor,
                shape = buttonShape,
                rapidHoldSpeed = rapidHoldSpeed,
                onTrigger = onIncrement,
                modifier = m
            )
        }

        if (isLeftHanded) {
            incBtn(Modifier.weight(2.4f))
            decBtn(Modifier.weight(1f))
        } else {
            decBtn(Modifier.weight(1f))
            incBtn(Modifier.weight(2.4f))
        }
    }
}

@Composable
private fun RepeatableActionButton(
    icon: ImageVector,
    iconSize: Dp,
    contentDescription: String,
    containerColor: Color,
    contentColor: Color,
    shape: Shape,
    rapidHoldSpeed: RapidHoldSpeed,
    onTrigger: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()
    val coroutineScope = rememberCoroutineScope()
    var isPressed by remember { mutableStateOf(false) }
    var repeatJob by remember { mutableStateOf<Job?>(null) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "ButtonScale"
    )

    Card(
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .fillMaxSize()
            .scale(scale)
            .pointerInput(rapidHoldSpeed) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        haptics.tick()
                        repeatJob = coroutineScope.launch {
                            delay(280)
                            var currentDelay = rapidHoldSpeed.initialDelayMs
                            while (true) {
                                haptics.rapidTick()
                                onTrigger()
                                delay(currentDelay)
                                if (currentDelay > rapidHoldSpeed.stepDelayMs) currentDelay -= 15L
                            }
                        }
                        val released = tryAwaitRelease()
                        isPressed = false
                        repeatJob?.cancel()
                        if (released) {
                            onTrigger()
                        }
                    }
                )
            }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = contentColor,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
