package com.hiashwinsharma.keisuki.data.repository

import com.hiashwinsharma.keisuki.core.model.ColorToken
import com.hiashwinsharma.keisuki.core.model.Counter
import com.hiashwinsharma.keisuki.core.model.SyncStatus
import com.hiashwinsharma.keisuki.data.auth.AuthRepository
import com.hiashwinsharma.keisuki.data.local.CounterDao
import com.hiashwinsharma.keisuki.data.local.CounterEntity
import com.hiashwinsharma.keisuki.data.sync.SyncScheduler
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

    suspend fun incrementCounter(id: String, step: Long = 1L) {
        val now = System.currentTimeMillis()
        counterDao.incrementCounter(id = id, step = step, updatedAt = now)
        syncScheduler.triggerImmediateSync()
    }

    suspend fun decrementCounter(id: String, step: Long = 1L) {
        val now = System.currentTimeMillis()
        counterDao.decrementCounter(id = id, step = step, updatedAt = now)
        syncScheduler.triggerImmediateSync()
    }

    suspend fun resetCounter(id: String) {
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
        title: String? = null,
        count: Long? = null,
        step: Long? = null,
        colorToken: ColorToken? = null
    ) {
        val existing = counterDao.getCounterById(id) ?: return
        val currentDomain = existing.toDomain()
        val updatedDomain = currentDomain.copy(
            title = title?.trim()?.ifEmpty { currentDomain.title } ?: currentDomain.title,
            count = count ?: currentDomain.count,
            step = step?.coerceAtLeast(1L) ?: currentDomain.step,
            colorToken = colorToken ?: currentDomain.colorToken,
            updatedAt = System.currentTimeMillis(),
            syncStatus = SyncStatus.PENDING_SYNC
        )
        counterDao.update(CounterEntity.fromDomain(updatedDomain))
        syncScheduler.triggerImmediateSync()
    }

    suspend fun deleteCounter(id: String) {
        val now = System.currentTimeMillis()
        counterDao.markForDeletion(id = id, updatedAt = now)
        syncScheduler.triggerImmediateSync()
    }

    suspend fun associateAnonymousDataOnSignIn(userId: String) {
        val now = System.currentTimeMillis()
        counterDao.associateAnonymousCounters(newUserId = userId, updatedAt = now)
        syncScheduler.triggerImmediateSync()
    }
}
