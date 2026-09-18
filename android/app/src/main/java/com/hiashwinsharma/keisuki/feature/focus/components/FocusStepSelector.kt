package com.hiashwinsharma.keisuki.feature.focus.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics
import com.hiashwinsharma.keisuki.core.model.ColorToken

@Composable
fun FocusStepSelector(
    currentStep: Long,
    colorToken: ColorToken,
    cornerRadius: Dp,
    onSelectStep: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()
    val quickSteps = listOf(1L, 5L, 10L, 25L, 50L, 100L)

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(quickSteps) { stepVal ->
            val isSelected = currentStep == stepVal
            FilterChip(
                selected = isSelected,
                onClick = {
                    haptics.pop()
                    onSelectStep(stepVal)
                },
                label = {
                    Text(
                        text = "±$stepVal",
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                shape = RoundedCornerShape(cornerRadius * 0.6f),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = if (colorToken.isCustomColor) colorToken.primaryColor else MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}
