package com.hiashwinsharma.keisuki.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics
import com.hiashwinsharma.keisuki.core.model.ColorToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCounterBottomSheet(
    defaultColorToken: ColorToken = ColorToken.DYNAMIC_PRIMARY,
    onDismiss: () -> Unit,
    onConfirm: (title: String, initialCount: Long, step: Long, colorToken: ColorToken) -> Unit
) {
    val haptics = rememberExpressiveHaptics()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusRequester = remember { FocusRequester() }

    var title by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1L) }
    var selectedColor by remember { mutableStateOf(defaultColorToken) }

    val quickSteps = listOf(1L, 5L, 10L, 25L)

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Clean Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("What are you counting?") },
                singleLine = true,
                textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (selectedColor.isCustomColor) selectedColor.primaryColor else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        val trimmed = title.trim().ifEmpty { "New Counter" }
                        haptics.pop()
                        onConfirm(trimmed, 0L, step, selectedColor)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Step Chips & Color Palette in single clean row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    quickSteps.forEach { stepVal ->
                        val isSelected = step == stepVal
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                haptics.tick()
                                step = stepVal
                            },
                            label = { Text("±$stepVal", fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = if (selectedColor.isCustomColor) selectedColor.primaryColor else MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val dynamicTokens = remember { ColorToken.entries.filter { it.isDynamic } }
            val fixedTokens = remember { ColorToken.entries.filter { !it.isDynamic } }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(dynamicTokens) { token ->
                    val isSelected = selectedColor == token
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(token.containerColor)
                            .clickable {
                                haptics.tick()
                                selectedColor = token
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
                                modifier = Modifier.size(18.dp)
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(token.containerColor)
                            .clickable {
                                haptics.tick()
                                selectedColor = token
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
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    val trimmed = title.trim().ifEmpty { "New Counter" }
                    haptics.pop()
                    onConfirm(trimmed, 0L, step, selectedColor)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedColor.isCustomColor) selectedColor.primaryColor else MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "Add Counter",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
