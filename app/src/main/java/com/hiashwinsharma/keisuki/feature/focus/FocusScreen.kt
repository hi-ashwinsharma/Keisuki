package com.hiashwinsharma.keisuki.feature.focus

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiashwinsharma.keisuki.core.designsystem.LocalAppCornerRadius
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics
import com.hiashwinsharma.keisuki.core.model.Counter
import com.hiashwinsharma.keisuki.core.model.SyncStatus
import com.hiashwinsharma.keisuki.core.ui.RollingNumberText
import com.hiashwinsharma.keisuki.data.preferences.UserPreferences
import com.hiashwinsharma.keisuki.feature.focus.components.CounterFocusOptionsBottomSheet
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(
    counter: Counter?,
    preferences: UserPreferences,
    onAction: (FocusUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()
    val coroutineScope = rememberCoroutineScope()
    val currentRadius = LocalAppCornerRadius.current

    var showOptionsSheet by remember { mutableStateOf(false) }
    var isEditingTitle by remember { mutableStateOf(false) }
    var editedTitle by remember { mutableStateOf("") }

    val currentCounter = counter

    // Milestone celebration effect
    var previousCount by remember { mutableStateOf<Long?>(null) }
    LaunchedEffect(currentCounter?.count, preferences.milestoneCelebrationEnabled) {
        val count = currentCounter?.count ?: return@LaunchedEffect
        if (previousCount != null && count != previousCount) {
            if (preferences.milestoneCelebrationEnabled && count > 0 && (count % 100L == 0L || count % 50L == 0L || count % 10L == 0L)) {
                haptics.milestone()
            }
        }
        previousCount = count
    }

    val quickSteps = listOf(1L, 5L, 10L, 25L, 50L, 100L)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    if (isEditingTitle) {
                        BasicTextField(
                            value = editedTitle,
                            onValueChange = { editedTitle = it },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            cursorBrush = SolidColor(currentCounter?.colorToken?.primaryColor ?: MaterialTheme.colorScheme.primary),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                if (editedTitle.isNotBlank() && currentCounter != null) {
                                    onAction(
                                        FocusUiAction.OnUpdateCounter(
                                            title = editedTitle,
                                            count = currentCounter.count,
                                            step = currentCounter.step,
                                            colorToken = currentCounter.colorToken
                                        )
                                    )
                                }
                                isEditingTitle = false
                            }),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    editedTitle = currentCounter?.title ?: ""
                                    isEditingTitle = true
                                }
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = currentCounter?.title ?: "Counter",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit title",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            haptics.tick()
                            onAction(FocusUiAction.OnBackClick)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    if (isEditingTitle) {
                        IconButton(
                            onClick = {
                                if (editedTitle.isNotBlank() && currentCounter != null) {
                                    onAction(
                                        FocusUiAction.OnUpdateCounter(
                                            title = editedTitle,
                                            count = currentCounter.count,
                                            step = currentCounter.step,
                                            colorToken = currentCounter.colorToken
                                        )
                                    )
                                }
                                isEditingTitle = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Save title",
                                tint = currentCounter?.colorToken?.primaryColor ?: MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        if (currentCounter?.syncStatus == SyncStatus.PENDING_SYNC) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = "Pending Sync",
                                tint = currentCounter.colorToken.primaryColor,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(20.dp)
                            )
                        }

                        IconButton(onClick = {
                            haptics.tick()
                            onAction(FocusUiAction.OnHistoryClick)
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = "Calendar History",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        IconButton(onClick = {
                            haptics.tick()
                            showOptionsSheet = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Options",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (currentCounter == null) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                val colorToken = currentCounter.colorToken

                var totalDragY by remember { mutableFloatStateOf(0f) }

                Column(
                    modifier = Modifier
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
                    // 1. HERO COUNTER DISPLAY CANVAS
                    Card(
                        shape = RoundedCornerShape(currentRadius * 1.25f),
                        colors = CardDefaults.cardColors(
                            containerColor = colorToken.containerColor,
                            contentColor = colorToken.onContainerColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                val isMilestone = currentCounter.count > 0 && (currentCounter.count % 10L == 0L)
                                AnimatedVisibility(
                                    visible = isMilestone,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(currentRadius * 0.6f),
                                        color = colorToken.primaryColor,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    ) {
                                        Text(
                                            text = "🎉 Milestone ${currentCounter.count}",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimary
                                            ),
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                        )
                                    }
                                }

                                RollingNumberText(
                                    count = currentCounter.count,
                                    style = MaterialTheme.typography.displayLarge.copy(
                                        fontSize = 108.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = (-3.0).sp,
                                        lineHeight = 108.sp
                                    ),
                                    color = colorToken.onContainerColor
                                )
                            }
                        }
                    }

                    // 2. INLINE STEP SELECTOR CHIPS
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(quickSteps) { stepVal ->
                            val isSelected = currentCounter.step == stepVal
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    haptics.pop()
                                    onAction(
                                        FocusUiAction.OnUpdateCounter(
                                            title = currentCounter.title,
                                            count = currentCounter.count,
                                            step = stepVal,
                                            colorToken = currentCounter.colorToken
                                        )
                                    )
                                },
                                label = {
                                    Text(
                                        text = "±$stepVal",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                shape = RoundedCornerShape(currentRadius * 0.6f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = if (colorToken.isCustomColor) colorToken.primaryColor else MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }

                    // 3. ERGONOMIC THUMB CONTROLS
                    var isIncrementPressed by remember { mutableStateOf(false) }
                    val incrementScale by animateFloatAsState(
                        targetValue = if (isIncrementPressed) 0.94f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "IncrementScale"
                    )
                    var incrementJob by remember { mutableStateOf<Job?>(null) }

                    var isDecrementPressed by remember { mutableStateOf(false) }
                    val decrementScale by animateFloatAsState(
                        targetValue = if (isDecrementPressed) 0.94f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "DecrementScale"
                    )
                    var decrementJob by remember { mutableStateOf<Job?>(null) }

                    val speed = preferences.rapidHoldSpeed
                    val buttonShape = if (preferences.isPillButtons) CircleShape else RoundedCornerShape(currentRadius * 1.1f)

                    val subtractButton = @Composable { btnModifier: Modifier ->
                        Card(
                            shape = buttonShape,
                            colors = CardDefaults.cardColors(
                                containerColor = if (colorToken.isCustomColor) colorToken.primaryColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceContainerHigh,
                                contentColor = colorToken.onContainerColor
                            ),
                            modifier = btnModifier
                                .fillMaxSize()
                                .scale(decrementScale)
                                .pointerInput(speed) {
                                    detectTapGestures(
                                        onPress = {
                                            isDecrementPressed = true
                                            haptics.tick()
                                            decrementJob = coroutineScope.launch {
                                                delay(280)
                                                var currentDelay = speed.initialDelayMs
                                                while (true) {
                                                    haptics.rapidTick()
                                                    onAction(FocusUiAction.OnDecrement)
                                                    delay(currentDelay)
                                                    if (currentDelay > speed.stepDelayMs) currentDelay -= 15L
                                                }
                                            }
                                            val released = tryAwaitRelease()
                                            isDecrementPressed = false
                                            decrementJob?.cancel()
                                            if (released) {
                                                onAction(FocusUiAction.OnDecrement)
                                            }
                                        }
                                    )
                                }
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrement",
                                    tint = colorToken.onContainerColor,
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                        }
                    }

                    val addButton = @Composable { btnModifier: Modifier ->
                        Card(
                            shape = buttonShape,
                            colors = CardDefaults.cardColors(
                                containerColor = if (colorToken.isCustomColor) colorToken.primaryColor else MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = btnModifier
                                .fillMaxSize()
                                .scale(incrementScale)
                                .pointerInput(speed) {
                                    detectTapGestures(
                                        onPress = {
                                            isIncrementPressed = true
                                            haptics.tick()
                                            incrementJob = coroutineScope.launch {
                                                delay(280)
                                                var currentDelay = speed.initialDelayMs
                                                while (true) {
                                                    haptics.rapidTick()
                                                    onAction(FocusUiAction.OnIncrement)
                                                    delay(currentDelay)
                                                    if (currentDelay > speed.stepDelayMs) currentDelay -= 15L
                                                }
                                            }
                                            val released = tryAwaitRelease()
                                            isIncrementPressed = false
                                            incrementJob?.cancel()
                                            if (released) {
                                                onAction(FocusUiAction.OnIncrement)
                                            }
                                        }
                                    )
                                }
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increment",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(42.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(88.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (preferences.isLeftHanded) {
                            addButton(Modifier.weight(2.4f))
                            subtractButton(Modifier.weight(1f))
                        } else {
                            subtractButton(Modifier.weight(1f))
                            addButton(Modifier.weight(2.4f))
                        }
                    }
                }
            }
        }
    }

    if (showOptionsSheet && currentCounter != null) {
        CounterFocusOptionsBottomSheet(
            counter = currentCounter,
            onDismiss = { showOptionsSheet = false },
            onColorChanged = { newColor ->
                onAction(
                    FocusUiAction.OnUpdateCounter(
                        title = currentCounter.title,
                        count = currentCounter.count,
                        step = currentCounter.step,
                        colorToken = newColor
                    )
                )
            },
            onReset = {
                onAction(FocusUiAction.OnReset)
            },
            onDelete = {
                onAction(FocusUiAction.OnDelete(onDeleted = { onAction(FocusUiAction.OnBackClick) }))
            }
        )
    }
}
