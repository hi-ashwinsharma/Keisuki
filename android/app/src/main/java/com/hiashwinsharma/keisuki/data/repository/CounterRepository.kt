package com.hiashwinsharma.keisuki.data.repository

import com.hiashwinsharma.keisuki.core.model.ColorToken
import com.hiashwinsharma.keisuki.core.model.Counter
import com.hiashwinsharma.keisuki.core.model.SyncStatus
import com.hiashwinsharma.keisuki.data.auth.AuthRepository
import com.hiashwinsharma.keisuki.data.local.CounterDao
import com.hiashwinsharma.keisuki.data.local.CounterEntity
import com.hiashwinsharma.keisuki.data.local.CounterEventDao
import com.hiashwinsharma.keisuki.data.local.CounterEventEntity
import com.hiashwinsharma.keisuki.data.sync.SyncScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class CounterRepository(
    private val counterDao: CounterDao,
    private val counterEventDao: CounterEventDao,
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

    val countersFlow: Flow<List<Counter>> = counterDao.getAllActiveCounters().map { entities ->
        entities.map { it.toDomain() }
    }

    fun getCounterByIdFlow(id: String): Flow<Counter?> {
        return counterDao.getCounterByIdFlow(id).map { it?.toDomain() }
    }

    fun getEventsForCounterBetween(counterId: String, startTime: Long, endTime: Long): Flow<List<CounterEventEntity>> {
        return counterEventDao.getEventsForCounterBetween(counterId, startTime, endTime)
    }

    fun getAllEventsBetween(startTime: Long, endTime: Long): Flow<List<CounterEventEntity>> {
        return counterEventDao.getAllEventsBetween(startTime, endTime)
    }

    fun getRecentEventsForCounter(counterId: String, limit: Int = 100): Flow<List<CounterEventEntity>> {
        return counterEventDao.getRecentEventsForCounter(counterId, limit)
    }

    fun getAllRecentEvents(limit: Int = 100): Flow<List<CounterEventEntity>> {
        return counterEventDao.getAllRecentEvents(limit)
    }

    suspend fun getLastEventBefore(counterId: String, startTime: Long): CounterEventEntity? {
        return counterEventDao.getLastEventBefore(counterId, startTime)
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
        counterEventDao.insertEvent(
            CounterEventEntity(
                counterId = newCounter.id,
                timestamp = now,
                count = initialCount,
                delta = initialCount,
                eventType = "CREATE"
            )
        )
        syncScheduler.scheduleDebouncedSync()
        return newCounter
    }

    suspend fun incrementCounter(id: String, step: Long = 1L) {
        val now = System.currentTimeMillis()
        counterDao.incrementCounter(id = id, step = step, updatedAt = now)
        val current = counterDao.getCounterById(id)
        if (current != null) {
            counterEventDao.insertEvent(
                CounterEventEntity(
                    counterId = id,
                    timestamp = now,
                    count = current.count,
                    delta = step,
                    eventType = "INCREMENT"
                )
            )
        }
        syncScheduler.scheduleDebouncedSync()
    }

    suspend fun decrementCounter(id: String, step: Long = 1L) {
        val now = System.currentTimeMillis()
        counterDao.decrementCounter(id = id, step = step, updatedAt = now)
        val current = counterDao.getCounterById(id)
        if (current != null) {
            counterEventDao.insertEvent(
                CounterEventEntity(
                    counterId = id,
                    timestamp = now,
                    count = current.count,
                    delta = -step,
                    eventType = "DECREMENT"
                )
            )
        }
        syncScheduler.scheduleDebouncedSync()
    }

    suspend fun resetCounter(id: String) {
        val now = System.currentTimeMillis()
        val existing = counterDao.getCounterById(id)
        val delta = if (existing != null) -existing.count else 0L
        counterDao.resetCounter(id = id, updatedAt = now)
        counterEventDao.insertEvent(
            CounterEventEntity(
                counterId = id,
                timestamp = now,
                count = 0L,
                delta = delta,
                eventType = "RESET"
            )
        )
        syncScheduler.scheduleDebouncedSync()
    }

    suspend fun resetAllCounters() {
        val now = System.currentTimeMillis()
        val counters = counterDao.getCountersBySyncStatus(SyncStatus.SYNCED) +
                counterDao.getCountersBySyncStatus(SyncStatus.PENDING_SYNC)
        counterDao.resetAllCounters(updatedAt = now)
        counters.forEach { counter ->
            counterEventDao.insertEvent(
                CounterEventEntity(
                    counterId = counter.id,
                    timestamp = now,
                    count = 0L,
                    delta = -counter.count,
                    eventType = "RESET"
                )
            )
        }
        syncScheduler.scheduleDebouncedSync()
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
        val targetCount = count ?: currentDomain.count
        val countChanged = targetCount != currentDomain.count
        val delta = targetCount - currentDomain.count

        val updatedDomain = currentDomain.copy(
            title = title?.trim()?.ifEmpty { currentDomain.title } ?: currentDomain.title,
            count = targetCount,
            step = step?.coerceAtLeast(1L) ?: currentDomain.step,
            colorToken = colorToken ?: currentDomain.colorToken,
            updatedAt = System.currentTimeMillis(),
            syncStatus = SyncStatus.PENDING_SYNC
        )
        counterDao.update(CounterEntity.fromDomain(updatedDomain))

        if (countChanged) {
            counterEventDao.insertEvent(
                CounterEventEntity(
                    counterId = id,
                    timestamp = updatedDomain.updatedAt,
                    count = targetCount,
                    delta = delta,
                    eventType = "EDIT"
                )
            )
        }
        syncScheduler.scheduleDebouncedSync()
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
