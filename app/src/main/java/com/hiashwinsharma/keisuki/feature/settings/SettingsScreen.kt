package com.hiashwinsharma.keisuki.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.RoundedCorner
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiashwinsharma.keisuki.R
import com.hiashwinsharma.keisuki.core.designsystem.LocalAppCornerRadius
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics
import com.hiashwinsharma.keisuki.data.auth.AuthState
import com.hiashwinsharma.keisuki.data.preferences.CounterSortOrder
import com.hiashwinsharma.keisuki.data.preferences.GridLayoutMode
import com.hiashwinsharma.keisuki.data.preferences.HapticIntensity
import com.hiashwinsharma.keisuki.data.preferences.RapidHoldSpeed
import com.hiashwinsharma.keisuki.data.preferences.ThemeMode
import com.hiashwinsharma.keisuki.data.preferences.UserPreferences
import com.hiashwinsharma.keisuki.feature.auth.AuthBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferences: UserPreferences,
    authState: AuthState,
    onAction: (SettingsUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()
    val currentRadius = LocalAppCornerRadius.current
    var showResetSheet by remember { mutableStateOf(false) }
    var showAuthSheet by remember { mutableStateOf(false) }
    val webClientId = stringResource(id = R.string.google_web_client_id)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        haptics.tick()
                        onAction(SettingsUiAction.OnBackClick)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. APPEARANCE & THEMING
            item {
                SettingsSectionHeader(title = "Appearance & Theming", icon = Icons.Default.ColorLens)
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(currentRadius),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Theme Mode",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(ThemeMode.entries) { mode ->
                                    val isSelected = preferences.themeMode == mode
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            haptics.pop()
                                            onAction(SettingsUiAction.SetThemeMode(mode))
                                        },
                                        label = { Text(mode.title, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                        leadingIcon = if (isSelected) {
                                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                        } else null,
                                        shape = RoundedCornerShape(currentRadius * 0.6f),
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                        SettingsToggleRow(
                            title = "Dynamic Colors (Monet)",
                            subtitle = "Sample vibrant colors from your wallpaper (Android 12+)",
                            icon = Icons.Default.Brightness4,
                            checked = preferences.isDynamicColor,
                            onCheckedChange = {
                                haptics.tick()
                                onAction(SettingsUiAction.SetDynamicColor(it))
                            }
                        )
                    }
                }
            }

            // 2. CORNER GEOMETRY & SHAPES
            item {
                SettingsSectionHeader(title = "Corner Geometry & Shapes", icon = Icons.Default.RoundedCorner)
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(currentRadius),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Corner Curvature",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Current: ${preferences.cornerRadiusDp.toInt()} dp",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (preferences.cornerRadiusDp > 0f) {
                                                haptics.tick()
                                                onAction(SettingsUiAction.SetCornerRadius((preferences.cornerRadiusDp - 2f).coerceAtLeast(0f)))
                                            }
                                        },
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease radius", modifier = Modifier.size(18.dp))
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceContainerHighest,
                                        modifier = Modifier.padding(horizontal = 2.dp)
                                    ) {
                                        Text(
                                            text = "${preferences.cornerRadiusDp.toInt()} dp",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            if (preferences.cornerRadiusDp < 36f) {
                                                haptics.tick()
                                                onAction(SettingsUiAction.SetCornerRadius((preferences.cornerRadiusDp + 2f).coerceAtMost(36f)))
                                            }
                                        },
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase radius", modifier = Modifier.size(18.dp))
                                    }
                                }
                            }

                            val presets = listOf(
                                "Sharp" to 4f,
                                "Compact" to 12f,
                                "Medium" to 20f,
                                "Expressive" to 28f,
                                "Full" to 34f
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(presets) { (name, r) ->
                                    val isSelected = preferences.cornerRadiusDp == r
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            haptics.pop()
                                            onAction(SettingsUiAction.SetCornerRadius(r))
                                        },
                                        label = { Text(name, fontSize = 12.sp) },
                                        shape = RoundedCornerShape(r.dp * 0.5f),
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                                        )
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                        SettingsToggleRow(
                            title = "Pill Buttons",
                            subtitle = "Make the focus screen Add and Subtract buttons pure pills",
                            icon = Icons.Default.Category,
                            checked = preferences.isPillButtons,
                            onCheckedChange = {
                                haptics.tick()
                                onAction(SettingsUiAction.SetPillButtons(it))
                            }
                        )
                    }
                }
            }

            // 3. ERGONOMICS & CONTROLS
            item {
                SettingsSectionHeader(title = "Ergonomics & Controls", icon = Icons.Default.PanTool)
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(currentRadius),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SettingsToggleRow(
                            title = "Left-Handed Mode",
                            subtitle = "Flips the dock: places the large Add button on the left",
                            icon = Icons.Default.TouchApp,
                            checked = preferences.isLeftHanded,
                            onCheckedChange = {
                                haptics.tick()
                                onAction(SettingsUiAction.SetLeftHanded(it))
                            }
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                        SettingsToggleRow(
                            title = "Volume Button Counting",
                            subtitle = "Use physical Volume Up / Down buttons in focus mode",
                            icon = Icons.AutoMirrored.Filled.VolumeUp,
                            checked = preferences.volumeKeysEnabled,
                            onCheckedChange = {
                                haptics.tick()
                                onAction(SettingsUiAction.SetVolumeKeysEnabled(it))
                            }
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                        SettingsToggleRow(
                            title = "Keep Screen Awake",
                            subtitle = "Prevents display from sleeping while in counter focus",
                            icon = Icons.Default.PhoneAndroid,
                            checked = preferences.keepScreenAwake,
                            onCheckedChange = {
                                haptics.tick()
                                onAction(SettingsUiAction.SetKeepScreenAwake(it))
                            }
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                        SettingsToggleRow(
                            title = "Full-Screen Swipe Gestures",
                            subtitle = "Swipe up anywhere to increment, swipe down to decrement",
                            icon = Icons.Default.PanTool,
                            checked = preferences.swipeGesturesEnabled,
                            onCheckedChange = {
                                haptics.tick()
                                onAction(SettingsUiAction.SetSwipeGesturesEnabled(it))
                            }
                        )
                    }
                }
            }

            // 4. HAPTICS & ACCELERATION
            item {
                SettingsSectionHeader(title = "Haptics & Acceleration", icon = Icons.Default.Vibration)
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(currentRadius),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Haptic Feedback Intensity",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(HapticIntensity.entries) { intensity ->
                                    val isSelected = preferences.hapticIntensity == intensity
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            onAction(SettingsUiAction.SetHapticIntensity(intensity))
                                            if (intensity != HapticIntensity.OFF) haptics.pop()
                                        },
                                        label = { Text(intensity.title, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                        shape = RoundedCornerShape(currentRadius * 0.6f),
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                        SettingsToggleRow(
                            title = "Milestone Celebrations",
                            subtitle = "Special crescendo vibration when hitting 10, 50, 100",
                            icon = Icons.Default.Vibration,
                            checked = preferences.milestoneCelebrationEnabled,
                            onCheckedChange = {
                                haptics.tick()
                                onAction(SettingsUiAction.SetMilestoneCelebrationEnabled(it))
                            }
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Rapid-Hold Acceleration Speed",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(RapidHoldSpeed.entries) { speed ->
                                    val isSelected = preferences.rapidHoldSpeed == speed
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            haptics.pop()
                                            onAction(SettingsUiAction.SetRapidHoldSpeed(speed))
                                        },
                                        label = { Text(speed.title, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                        shape = RoundedCornerShape(currentRadius * 0.6f),
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onTertiary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. HOME GRID STYLE
            item {
                SettingsSectionHeader(title = "Home Screen Layout", icon = Icons.Default.GridOn)
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(currentRadius),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Grid Columns",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            GridLayoutMode.entries.forEach { mode ->
                                val isSelected = preferences.gridLayout == mode
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        haptics.pop()
                                        onAction(SettingsUiAction.SetGridLayout(mode))
                                    },
                                    label = { Text(mode.title, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                    shape = RoundedCornerShape(currentRadius * 0.6f),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                        Text(
                            text = "Sort Counters By",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(CounterSortOrder.entries) { order ->
                                val isSelected = preferences.sortOrder == order
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        haptics.pop()
                                        onAction(SettingsUiAction.SetSortOrder(order))
                                    },
                                    label = { Text(order.title, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                    leadingIcon = if (isSelected) {
                                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                    } else null,
                                    shape = RoundedCornerShape(currentRadius * 0.6f),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 6. ACCOUNT & DATA
            item {
                SettingsSectionHeader(title = "Account & Data", icon = Icons.Default.AccountCircle)
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(currentRadius),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = if (authState.isAuthenticated) Icons.Default.AccountCircle else Icons.Outlined.AccountCircle,
                                    contentDescription = null,
                                    tint = if (authState.isAuthenticated) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = if (authState.isAuthenticated) authState.displayName else "Offline Mode",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (authState.isAuthenticated) (authState.user?.email ?: "Signed In") else "Counters stored locally on device",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Button(
                                onClick = {
                                    haptics.tick()
                                    showAuthSheet = true
                                },
                                shape = RoundedCornerShape(currentRadius * 0.6f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (authState.isAuthenticated) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.primary,
                                    contentColor = if (authState.isAuthenticated) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text(if (authState.isAuthenticated) "Manage" else "Sign In")
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                        OutlinedButton(
                            onClick = {
                                haptics.pop()
                                showResetSheet = true
                            },
                            shape = RoundedCornerShape(currentRadius * 0.6f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Reset All Counter Values to 0", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (showResetSheet) {
        ModalBottomSheet(
            onDismissRequest = { showResetSheet = false },
            sheetState = rememberModalBottomSheetState(),
            shape = RoundedCornerShape(topStart = currentRadius, topEnd = currentRadius),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Reset All Counters?",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "This will set the current count of all your active counters back to 0. Counter titles and custom steps will be preserved.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { showResetSheet = false },
                        shape = RoundedCornerShape(currentRadius * 0.6f),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            haptics.milestone()
                            onAction(SettingsUiAction.ResetAllCounters)
                            showResetSheet = false
                        },
                        shape = RoundedCornerShape(currentRadius * 0.6f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                    ) {
                        Text("Reset All", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showAuthSheet) {
        AuthBottomSheet(
            authState = authState,
            isSyncPending = false,
            onDismiss = { showAuthSheet = false },
            onSignInWithGoogle = { onAction(SettingsUiAction.SignInWithGoogle(webClientId)) },
            onSignOut = { onAction(SettingsUiAction.SignOut) }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.padding(end = 8.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}
