package com.hiashwinsharma.keisuki.data.repository

import com.hiashwinsharma.keisuki.data.auth.AuthRepository
import com.hiashwinsharma.keisuki.data.local.CounterDao
import com.hiashwinsharma.keisuki.data.local.CounterEntity
import com.hiashwinsharma.keisuki.data.sync.SyncScheduler
import com.hiashwinsharma.keisuki.model.ColorToken
import com.hiashwinsharma.keisuki.model.Counter
import com.hiashwinsharma.keisuki.model.SyncStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class CounterRepository(
    private val counterDao: CounterDao,
    private val authRepository: AuthRepository,
    private val syncScheduler: SyncScheduler
) {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                counterDao.fixMissingCreatedAt()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * SSOT: The UI observes the Room database exclusively.
     * Pending deletes are filtered out in DAO query so UI reflects deletions immediately.
     */
    val countersFlow: Flow<List<Counter>> = counterDao.getAllActiveCounters().map { entities ->
        entities.map { it.toDomain() }
    }

    fun getCounterByIdFlow(id: String): Flow<Counter?> {
        return counterDao.getCounterByIdFlow(id).map { it?.toDomain() }
    }

    suspend fun createCounter(
        title: String,
        initialCount: Long = 0L,
        step: Long = 1L,
        colorToken: ColorToken = ColorToken.DYNAMIC_PRIMARY
    ): Counter {
        val currentUserId = authRepository.currentUserId
        val now = System.currentTimeMillis()
        val newCounter = Counter(
            id = UUID.randomUUID().toString(),
            userId = currentUserId,
            title = title.trim().ifEmpty { "New Counter" },
            count = initialCount,
            step = step.coerceAtLeast(1L),
            colorToken = colorToken,
            createdAt = now,
            updatedAt = now,
            syncStatus = SyncStatus.PENDING_SYNC
        )

        counterDao.upsert(CounterEntity.fromDomain(newCounter))
        syncScheduler.triggerImmediateSync()
        return newCounter
    }

    suspend fun increment(id: String, customStep: Long? = null) {
        val now = System.currentTimeMillis()
        val counter = counterDao.getCounterById(id) ?: return
        val step = customStep ?: counter.step
        counterDao.incrementCounter(id = id, step = step, updatedAt = now)
        syncScheduler.triggerImmediateSync()
    }

    suspend fun decrement(id: String, customStep: Long? = null) {
        val now = System.currentTimeMillis()
        val counter = counterDao.getCounterById(id) ?: return
        val step = customStep ?: counter.step
        counterDao.decrementCounter(id = id, step = step, updatedAt = now)
        syncScheduler.triggerImmediateSync()
    }

    suspend fun reset(id: String) {
        val now = System.currentTimeMillis()
        counterDao.resetCounter(id = id, updatedAt = now)
        syncScheduler.triggerImmediateSync()
    }

    suspend fun resetAllCounters() {
        val now = System.currentTimeMillis()
        counterDao.resetAllCounters(updatedAt = now)
        syncScheduler.triggerImmediateSync()
    }

    suspend fun updateCounter(
        id: String,
        title: String,
        count: Long,
        step: Long,
        colorToken: ColorToken
    ) {
        val existing = counterDao.getCounterById(id) ?: return
        val updated = existing.copy(
            title = title.trim().ifEmpty { existing.title },
            count = count,
            step = step.coerceAtLeast(1L),
            colorToken = colorToken.id,
            createdAt = if (existing.createdAt > 0L) existing.createdAt else existing.updatedAt,
            updatedAt = System.currentTimeMillis(),
            syncStatus = SyncStatus.PENDING_SYNC
        )
        counterDao.upsert(updated)
        syncScheduler.triggerImmediateSync()
    }

    suspend fun delete(id: String) {
        val now = System.currentTimeMillis()
        counterDao.markForDeletion(id = id, updatedAt = now)
        syncScheduler.triggerImmediateSync()
    }

    suspend fun onUserSignedIn(newUserId: String) {
        counterDao.associateAnonymousCounters(newUserId)
        syncScheduler.triggerImmediateSync()
    }
}
