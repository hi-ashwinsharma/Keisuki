package com.hiashwinsharma.keisuki.feature.history.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryTopBar(
    counterTitle: String,
    dateDisplayLabel: String,
    isLockedToCounter: Boolean,
    hasMultipleCounters: Boolean,
    primaryThemeColor: Color,
    cornerRadius: Dp,
    onBackClick: () -> Unit,
    onJumpToTodayClick: () -> Unit,
    onDatePickerClick: () -> Unit,
    onSwitchCounterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()

    TopAppBar(
        modifier = modifier,
        title = {
            Column(
                modifier = if (!isLockedToCounter && hasMultipleCounters) {
                    Modifier
                        .clip(RoundedCornerShape(cornerRadius * 0.5f))
                        .clickable {
                            haptics.tick()
                            onSwitchCounterClick()
                        }
                        .padding(vertical = 2.dp, horizontal = 4.dp)
                } else Modifier
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$counterTitle History",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (!isLockedToCounter && hasMultipleCounters) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Switch Counter",
                            tint = primaryThemeColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Text(
                    text = dateDisplayLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                haptics.tick()
                onBackClick()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            IconButton(onClick = {
                haptics.tick()
                onJumpToTodayClick()
            }) {
                Icon(
                    imageVector = Icons.Default.Today,
                    contentDescription = "Jump to Today",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = {
                haptics.tick()
                onDatePickerClick()
            }) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Select Date",
                    tint = primaryThemeColor
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}
