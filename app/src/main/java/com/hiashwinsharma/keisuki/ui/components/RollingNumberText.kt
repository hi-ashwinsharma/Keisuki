package com.hiashwinsharma.keisuki.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset

@Composable
fun RollingNumberText(
    count: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.displayLarge,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    AnimatedContent(
        targetState = count,
        transitionSpec = {
            val springSpec = spring<IntOffset>(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
            val fadeSpec = spring<Float>(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
            if (targetState > initialState) {
                (slideInVertically(animationSpec = springSpec) { height -> (height * 0.8f).toInt() } +
                 fadeIn(animationSpec = fadeSpec)).togetherWith(
                    slideOutVertically(animationSpec = springSpec) { height -> (-height * 0.8f).toInt() } +
                    fadeOut(animationSpec = fadeSpec)
                )
            } else {
                (slideInVertically(animationSpec = springSpec) { height -> (-height * 0.8f).toInt() } +
                 fadeIn(animationSpec = fadeSpec)).togetherWith(
                    slideOutVertically(animationSpec = springSpec) { height -> (height * 0.8f).toInt() } +
                    fadeOut(animationSpec = fadeSpec)
                )
            }
        },
        label = "CountAnimation",
        modifier = modifier
    ) { currentCount ->
        Text(
            text = currentCount.toString(),
            style = style,
            color = color
        )
    }
}
