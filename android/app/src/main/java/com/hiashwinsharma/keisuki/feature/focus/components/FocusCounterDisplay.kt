package com.hiashwinsharma.keisuki.feature.focus.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiashwinsharma.keisuki.core.model.ColorToken
import com.hiashwinsharma.keisuki.core.ui.RollingNumberText

@Composable
fun FocusCounterDisplay(
    count: Long,
    colorToken: ColorToken,
    cornerRadius: Dp,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(cornerRadius * 1.25f),
        colors = CardDefaults.cardColors(
            containerColor = colorToken.containerColor,
            contentColor = colorToken.onContainerColor
        ),
        modifier = modifier.fillMaxWidth()
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
                val isMilestone = count > 0 && (count % 10L == 0L)
                AnimatedVisibility(
                    visible = isMilestone,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Surface(
                        shape = RoundedCornerShape(cornerRadius * 0.6f),
                        color = colorToken.primaryColor,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "🎉 Milestone $count",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }

                RollingNumberText(
                    count = count,
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
}
