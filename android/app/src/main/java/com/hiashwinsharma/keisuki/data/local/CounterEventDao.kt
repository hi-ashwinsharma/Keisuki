package com.hiashwinsharma.keisuki.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CounterEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<CounterEventEntity>)

    @Query("SELECT * FROM counter_events WHERE counterId = :counterId AND timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp ASC")
    fun getEventsForCounterBetween(counterId: String, startTime: Long, endTime: Long): Flow<List<CounterEventEntity>>

    @Query("SELECT * FROM counter_events WHERE timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp ASC")
    fun getAllEventsBetween(startTime: Long, endTime: Long): Flow<List<CounterEventEntity>>

    @Query("SELECT * FROM counter_events WHERE counterId = :counterId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentEventsForCounter(counterId: String, limit: Int = 100): Flow<List<CounterEventEntity>>

    @Query("SELECT * FROM counter_events ORDER BY timestamp DESC LIMIT :limit")
    fun getAllRecentEvents(limit: Int = 100): Flow<List<CounterEventEntity>>

    @Query("SELECT * FROM counter_events WHERE counterId = :counterId AND timestamp < :startTime ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastEventBefore(counterId: String, startTime: Long): CounterEventEntity?

    @Query("DELETE FROM counter_events WHERE counterId = :counterId")
    suspend fun deleteEventsForCounter(counterId: String)

    @Query("DELETE FROM counter_events WHERE counterId IN (:counterIds)")
    suspend fun deleteEventsForCounters(counterIds: List<String>)
}
