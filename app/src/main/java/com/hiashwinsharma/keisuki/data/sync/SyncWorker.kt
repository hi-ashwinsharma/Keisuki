package com.hiashwinsharma.keisuki.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.hiashwinsharma.keisuki.core.model.SyncStatus
import com.hiashwinsharma.keisuki.data.local.AppDatabase
import com.hiashwinsharma.keisuki.data.local.CounterEntity
import com.hiashwinsharma.keisuki.data.remote.FirestoreService

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val database = AppDatabase.getInstance(appContext)
    private val counterDao = database.counterDao()
    private val firestoreService = FirestoreService()
    private val firebaseAuth = FirebaseAuth.getInstance()

    override suspend fun doWork(): Result {
        val user = firebaseAuth.currentUser ?: return Result.success()
        val userId = user.uid

        return try {
            // 1. Process PENDING_DELETE records
            val pendingDelete = counterDao.getCountersBySyncStatus(SyncStatus.PENDING_DELETE)
            for (entity in pendingDelete) {
                val deleteResult = firestoreService.deleteCounter(userId, entity.id)
                if (deleteResult.isSuccess) {
                    counterDao.deletePermanently(entity.id)
                }
            }

            // 2. Process PENDING_SYNC records (Push local mutations to Firestore)
            val pendingSync = counterDao.getCountersBySyncStatus(SyncStatus.PENDING_SYNC)
            for (entity in pendingSync) {
                val domain = entity.toDomain().copy(userId = userId)
                val uploadResult = firestoreService.uploadCounter(userId, domain)
                if (uploadResult.isSuccess) {
                    val syncedEntity = entity.copy(
                        userId = userId,
                        syncStatus = SyncStatus.SYNCED
                    )
                    counterDao.upsert(syncedEntity)
                }
            }

            // 3. Pull Remote Changes from Firestore (Bi-directional delta sync)
            val remoteCountersResult = firestoreService.fetchAllRemoteCounters(userId)
            if (remoteCountersResult.isSuccess) {
                val remoteCounters = remoteCountersResult.getOrDefault(emptyList())
                for (remote in remoteCounters) {
                    val local = counterDao.getCounterById(remote.id)
                    if (local == null) {
                        counterDao.upsert(
                            CounterEntity.fromDomain(remote).copy(syncStatus = SyncStatus.SYNCED)
                        )
                    } else if (local.syncStatus == SyncStatus.SYNCED) {
                        if (remote.updatedAt >= local.updatedAt) {
                            counterDao.upsert(
                                CounterEntity.fromDomain(remote).copy(syncStatus = SyncStatus.SYNCED)
                            )
                        }
                    } else if (local.syncStatus == SyncStatus.PENDING_SYNC) {
                        if (remote.updatedAt > local.updatedAt) {
                            counterDao.upsert(
                                CounterEntity.fromDomain(remote).copy(syncStatus = SyncStatus.SYNCED)
                            )
                        } else {
                            firestoreService.uploadCounter(userId, local.toDomain())
                            counterDao.upsert(local.copy(syncStatus = SyncStatus.SYNCED))
                        }
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
