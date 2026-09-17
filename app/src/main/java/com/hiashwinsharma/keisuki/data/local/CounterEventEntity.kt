package com.hiashwinsharma.keisuki.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "counter_events",
    indices = [
        Index(value = ["counterId"]),
        Index(value = ["timestamp"])
    ]
)
data class CounterEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val counterId: String,
    val timestamp: Long,
    val count: Long,
    val delta: Long,
    val eventType: String
)
