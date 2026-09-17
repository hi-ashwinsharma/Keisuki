package com.hiashwinsharma.keisuki.feature.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics
import com.hiashwinsharma.keisuki.data.auth.AuthState
import com.hiashwinsharma.keisuki.data.preferences.CounterSortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    authState: AuthState,
    isSyncPending: Boolean,
    currentSortOrder: CounterSortOrder,
    onSortSelected: (CounterSortOrder) -> Unit,
    onAuthClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()
    var showSortMenu by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier,
        title = {
            Column {
                Text(
                    text = "Keisuki",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (authState.isAuthenticated) authState.displayName else "Offline Mode",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        actions = {
            if (isSyncPending) {
                IconButton(onClick = { haptics.tick(); onAuthClick() }, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.CloudSync, contentDescription = "Syncing", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(4.dp))
            }

            Box {
                IconButton(onClick = { haptics.tick(); showSortMenu = true }, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort counters", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(22.dp))
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false },
                    shape = RoundedCornerShape(16.dp),
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ) {
                    Text(
                        text = "Sort By",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    CounterSortOrder.entries.forEach { order ->
                        val isSelected = currentSortOrder == order
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = order.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            trailingIcon = if (isSelected) {
                                { Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp)) }
                            } else null,
                            onClick = {
                                haptics.pop()
                                onSortSelected(order)
                                showSortMenu = false
                            }
                        )
                    }
                }
            }

            IconButton(onClick = { haptics.tick(); onHistoryClick() }, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Outlined.CalendarMonth, contentDescription = "Calendar History", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(22.dp))
            }

            IconButton(onClick = { haptics.tick(); onAuthClick() }, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = if (authState.isAuthenticated) Icons.Default.AccountCircle else Icons.Outlined.AccountCircle,
                    contentDescription = "Account",
                    tint = if (authState.isAuthenticated) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(22.dp)
                )
            }

            IconButton(onClick = { haptics.tick(); onSettingsClick() }, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(22.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}
