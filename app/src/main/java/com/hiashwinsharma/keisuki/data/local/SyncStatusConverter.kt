package com.hiashwinsharma.keisuki.data.local

import androidx.room.TypeConverter
import com.hiashwinsharma.keisuki.core.model.SyncStatus

class SyncStatusConverter {
    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String {
        return status.name
    }

    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus {
        return try {
            SyncStatus.valueOf(value)
        } catch (e: Exception) {
            SyncStatus.PENDING_SYNC
        }
    }
}
