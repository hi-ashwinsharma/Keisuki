package com.hiashwinsharma.keisuki.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics
import com.hiashwinsharma.keisuki.core.model.ColorToken

@Composable
fun ColorPalettePicker(
    selectedColor: ColorToken,
    onSelectColor: (ColorToken) -> Unit,
    modifier: Modifier = Modifier,
    itemSize: Dp = 38.dp
) {
    val haptics = rememberExpressiveHaptics()
    val dynamicTokens = remember { ColorToken.entries.filter { it.isDynamic } }
    val fixedTokens = remember { ColorToken.entries.filter { !it.isDynamic } }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(dynamicTokens) { token ->
            val isSelected = selectedColor == token
            Box(
                modifier = Modifier
                    .size(itemSize)
                    .clip(CircleShape)
                    .background(token.containerColor)
                    .clickable {
                        haptics.tick()
                        onSelectColor(token)
                    }
                    .then(
                        if (isSelected) {
                            Modifier.border(2.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = token.onContainerColor,
                        modifier = Modifier.size(itemSize * 0.5f)
                    )
                }
            }
        }

        item {
            VerticalDivider(
                modifier = Modifier
                    .height(24.dp)
                    .padding(horizontal = 4.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        items(fixedTokens) { token ->
            val isSelected = selectedColor == token
            Box(
                modifier = Modifier
                    .size(itemSize)
                    .clip(CircleShape)
                    .background(token.containerColor)
                    .clickable {
                        haptics.tick()
                        onSelectColor(token)
                    }
                    .then(
                        if (isSelected) {
                            Modifier.border(2.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = token.onContainerColor,
                        modifier = Modifier.size(itemSize * 0.5f)
                    )
                }
            }
        }
    }
}
