package com.hiashwinsharma.keisuki.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.model.Counter
import com.hiashwinsharma.keisuki.data.preferences.GridLayoutMode

@Composable
fun HomeCounterGrid(
    counters: List<Counter>,
    gridLayoutMode: GridLayoutMode,
    onCounterClick: (String) -> Unit,
    onIncrement: (String) -> Unit,
    onDecrement: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val columns = when (gridLayoutMode) {
        GridLayoutMode.TWO_COLUMNS -> GridCells.Fixed(2)
        GridLayoutMode.ONE_COLUMN -> GridCells.Fixed(1)
    }

    LazyVerticalGrid(
        columns = columns,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(counters, key = { it.id }) { counter ->
            BentoCounterCard(
                counter = counter,
                onClick = { onCounterClick(counter.id) },
                onIncrement = { onIncrement(counter.id) },
                onDecrement = { onDecrement(counter.id) }
            )
        }
    }
}
