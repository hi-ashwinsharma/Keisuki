package com.hiashwinsharma.keisuki.model

data class Counter(
    val id: String,
    val userId: String? = null,
    val title: String,
    val count: Long = 0L,
    val step: Long = 1L,
    val colorToken: ColorToken = ColorToken.ELECTRIC_ROSE,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncStatus: SyncStatus = SyncStatus.PENDING_SYNC
)
