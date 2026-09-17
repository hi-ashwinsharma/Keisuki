package com.hiashwinsharma.keisuki.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiashwinsharma.keisuki.model.Counter
import com.hiashwinsharma.keisuki.model.SyncStatus

import com.hiashwinsharma.keisuki.ui.theme.LocalAppCornerRadius

@Composable
fun BentoCounterCard(
    counter: Counter,
    onClick: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()
    val colorToken = counter.colorToken
    val currentRadius = LocalAppCornerRadius.current

    Card(
        onClick = {
            haptics.tick()
            onClick()
        },
        shape = RoundedCornerShape(currentRadius),
        colors = CardDefaults.cardColors(
            containerColor = colorToken.containerColor,
            contentColor = colorToken.onContainerColor
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Title and Sync Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = counter.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorToken.onContainerColor
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (counter.syncStatus == SyncStatus.PENDING_SYNC) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(colorToken.onContainerColor.copy(alpha = 0.6f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Prominent Count with Rolling Number Transition
            RollingNumberText(
                count = counter.count,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-1.0).sp
                ),
                color = colorToken.onContainerColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Step hint & Mini Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "±${counter.step}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = colorToken.onContainerColor.copy(alpha = 0.85f),
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalIconButton(
                        onClick = {
                            haptics.tick()
                            onDecrement()
                        },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = colorToken.onContainerColor.copy(alpha = 0.12f),
                            contentColor = colorToken.onContainerColor
                        ),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrement",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    FilledIconButton(
                        onClick = {
                            haptics.tick()
                            onIncrement()
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = colorToken.onContainerColor.copy(alpha = 0.22f),
                            contentColor = colorToken.onContainerColor
                        ),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increment",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
