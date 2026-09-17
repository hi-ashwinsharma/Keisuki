package com.hiashwinsharma.keisuki.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hiashwinsharma.keisuki.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {

    @Query("SELECT * FROM counters WHERE syncStatus != 'PENDING_DELETE' ORDER BY updatedAt DESC")
    fun getAllActiveCounters(): Flow<List<CounterEntity>>

    @Query("SELECT * FROM counters WHERE id = :id AND syncStatus != 'PENDING_DELETE' LIMIT 1")
    fun getCounterByIdFlow(id: String): Flow<CounterEntity?>

    @Query("SELECT * FROM counters WHERE id = :id LIMIT 1")
    suspend fun getCounterById(id: String): CounterEntity?

    @Query("SELECT * FROM counters WHERE syncStatus = :syncStatus")
    suspend fun getCountersBySyncStatus(syncStatus: SyncStatus): List<CounterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(counter: CounterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(counters: List<CounterEntity>)

    @Update
    suspend fun update(counter: CounterEntity)

    @Query("UPDATE counters SET count = count + :step, updatedAt = :updatedAt, syncStatus = 'PENDING_SYNC' WHERE id = :id")
    suspend fun incrementCounter(id: String, step: Long, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE counters SET count = count - :step, updatedAt = :updatedAt, syncStatus = 'PENDING_SYNC' WHERE id = :id")
    suspend fun decrementCounter(id: String, step: Long, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE counters SET count = 0, updatedAt = :updatedAt, syncStatus = 'PENDING_SYNC' WHERE id = :id")
    suspend fun resetCounter(id: String, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE counters SET count = 0, updatedAt = :updatedAt, syncStatus = 'PENDING_SYNC' WHERE syncStatus != 'PENDING_DELETE'")
    suspend fun resetAllCounters(updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE counters SET syncStatus = 'PENDING_DELETE', updatedAt = :updatedAt WHERE id = :id")
    suspend fun markForDeletion(id: String, updatedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM counters WHERE id = :id")
    suspend fun deletePermanently(id: String)

    @Query("DELETE FROM counters WHERE id IN (:ids)")
    suspend fun deletePermanentlyAll(ids: List<String>)

    @Query("UPDATE counters SET userId = :newUserId, syncStatus = 'PENDING_SYNC', updatedAt = :updatedAt WHERE userId IS NULL OR userId = 'anonymous'")
    suspend fun associateAnonymousCounters(newUserId: String, updatedAt: Long = System.currentTimeMillis())

    @Query("SELECT MAX(updatedAt) FROM counters")
    suspend fun getLatestUpdatedAt(): Long?

    @Query("UPDATE counters SET createdAt = updatedAt WHERE createdAt = 0 OR createdAt IS NULL")
    suspend fun fixMissingCreatedAt()
}
