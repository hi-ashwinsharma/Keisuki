package com.hiashwinsharma.keisuki.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hiashwinsharma.keisuki.R
import com.hiashwinsharma.keisuki.data.preferences.CounterSortOrder
import com.hiashwinsharma.keisuki.data.preferences.GridLayoutMode
import com.hiashwinsharma.keisuki.model.ColorToken
import com.hiashwinsharma.keisuki.ui.components.BentoCounterCard
import com.hiashwinsharma.keisuki.ui.components.rememberExpressiveHaptics
import com.hiashwinsharma.keisuki.ui.screens.auth.AuthBottomSheet
import com.hiashwinsharma.keisuki.ui.theme.LocalAppCornerRadius

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterGridScreen(
    viewModel: CounterGridViewModel,
    onCounterClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val haptics = rememberExpressiveHaptics()
    val currentRadius = LocalAppCornerRadius.current

    var showAddDialog by remember { mutableStateOf(false) }
    var showAuthSheet by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    val webClientId = stringResource(id = R.string.google_web_client_id)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
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
                            text = if (uiState.authState.isAuthenticated) {
                                uiState.authState.displayName
                            } else {
                                "Offline Mode"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    if (uiState.isSyncPending) {
                        IconButton(
                            onClick = {
                                haptics.tick()
                                showAuthSheet = true
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = "Syncing",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    // Sort Action Button & Menu
                    Box {
                        IconButton(
                            onClick = {
                                haptics.tick()
                                showSortMenu = true
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = "Sort counters",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(22.dp)
                            )
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
                                val isSelected = uiState.preferences.sortOrder == order
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = order.title,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            ),
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    trailingIcon = if (isSelected) {
                                        {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    } else null,
                                    onClick = {
                                        haptics.pop()
                                        viewModel.setSortOrder(order)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Settings Action Button
                    IconButton(
                        onClick = {
                            haptics.tick()
                            onSettingsClick()
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                haptics.tick()
                                showAuthSheet = true
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (uiState.authState.isAuthenticated) Icons.Default.AccountCircle else Icons.Outlined.AccountCircle,
                                contentDescription = "Profile",
                                tint = if (uiState.authState.isAuthenticated) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )

                            if (!uiState.authState.isAuthenticated) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Sign in",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    haptics.pop()
                    showAddDialog = true
                },
                shape = RoundedCornerShape(currentRadius),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                modifier = Modifier.size(68.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Counter",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                // Keep background clean while initial database load finishes
            } else if (uiState.counters.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(currentRadius * 1.3f))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.InvertColors,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(52.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(id = R.string.no_counters_title),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(id = R.string.no_counters_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                val columnCount = if (uiState.preferences.gridLayout == GridLayoutMode.ONE_COLUMN) 1 else 2
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columnCount),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.counters, key = { it.id }) { counter ->
                        BentoCounterCard(
                            counter = counter,
                            onClick = { onCounterClick(counter.id) },
                            onIncrement = { viewModel.increment(counter.id) },
                            onDecrement = { viewModel.decrement(counter.id) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        val nextDefaultColor = uiState.counters.firstOrNull()?.colorToken?.nextColor() ?: ColorToken.DYNAMIC_PRIMARY
        AddCounterBottomSheet(
            defaultColorToken = nextDefaultColor,
            onDismiss = { showAddDialog = false },
            onConfirm = { title, initialCount, step, colorToken ->
                viewModel.createCounter(title, initialCount, step, colorToken)
                showAddDialog = false
            }
        )
    }

    if (showAuthSheet) {
        AuthBottomSheet(
            authState = uiState.authState,
            isSyncPending = uiState.isSyncPending,
            onDismiss = { showAuthSheet = false },
            onSignInWithGoogle = { viewModel.signInWithGoogle(webClientId) },
            onSignOut = { viewModel.signOut() }
        )
    }
}
