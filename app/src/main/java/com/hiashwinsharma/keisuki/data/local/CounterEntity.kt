package com.hiashwinsharma.keisuki.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hiashwinsharma.keisuki.model.ColorToken
import com.hiashwinsharma.keisuki.model.Counter
import com.hiashwinsharma.keisuki.model.SyncStatus

@Entity(tableName = "counters")
data class CounterEntity(
    @PrimaryKey
    val id: String,
    val userId: String?,
    val title: String,
    val count: Long,
    val step: Long,
    val colorToken: String,
    @ColumnInfo(defaultValue = "0")
    val createdAt: Long = 0L,
    val updatedAt: Long,
    val syncStatus: SyncStatus
) {
    fun toDomain(): Counter {
        val effectiveCreatedAt = if (createdAt > 0L) createdAt else updatedAt
        return Counter(
            id = id,
            userId = userId,
            title = title,
            count = count,
            step = step,
            colorToken = ColorToken.fromId(colorToken),
            createdAt = effectiveCreatedAt,
            updatedAt = updatedAt,
            syncStatus = syncStatus
        )
    }

    companion object {
        fun fromDomain(counter: Counter): CounterEntity {
            return CounterEntity(
                id = counter.id,
                userId = counter.userId,
                title = counter.title,
                count = counter.count,
                step = counter.step,
                colorToken = counter.colorToken.id,
                createdAt = counter.createdAt,
                updatedAt = counter.updatedAt,
                syncStatus = counter.syncStatus
            )
        }
    }
}
