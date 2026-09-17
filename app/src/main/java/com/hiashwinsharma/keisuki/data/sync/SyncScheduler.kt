package com.hiashwinsharma.keisuki.data.sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class SyncScheduler(private val context: Context) {

    private val workManager = WorkManager.getInstance(context)

    private val syncConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun triggerImmediateSync() {
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(syncConstraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniqueWork(
            SYNC_WORK_ONE_TIME_NAME,
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }

    fun schedulePeriodicSync(intervalMinutes: Long = 15) {
        val periodicRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            intervalMinutes,
            TimeUnit.MINUTES
        )
            .setConstraints(syncConstraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            SYNC_WORK_PERIODIC_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )
    }

    fun cancelAllSync() {
        workManager.cancelUniqueWork(SYNC_WORK_ONE_TIME_NAME)
        workManager.cancelUniqueWork(SYNC_WORK_PERIODIC_NAME)
    }

    companion object {
        private const val SYNC_WORK_ONE_TIME_NAME = "one_time_counter_sync"
        private const val SYNC_WORK_PERIODIC_NAME = "periodic_counter_sync"
    }
}
