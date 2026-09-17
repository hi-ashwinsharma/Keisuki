package com.hiashwinsharma.keisuki

import android.app.Application
import com.hiashwinsharma.keisuki.data.auth.AuthRepository
import com.hiashwinsharma.keisuki.data.local.AppDatabase
import com.hiashwinsharma.keisuki.data.preferences.UserPreferencesRepository
import com.hiashwinsharma.keisuki.data.repository.CounterRepository
import com.hiashwinsharma.keisuki.data.sync.SyncScheduler

class KeisukiApp : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var authRepository: AuthRepository
        private set

    lateinit var userPreferencesRepository: UserPreferencesRepository
        private set

    lateinit var syncScheduler: SyncScheduler
        private set

    lateinit var counterRepository: CounterRepository
        private set

    override fun onCreate() {
        super.onCreate()

        database = AppDatabase.getInstance(this)
        authRepository = AuthRepository(this)
        userPreferencesRepository = UserPreferencesRepository(this)
        syncScheduler = SyncScheduler(this)
        counterRepository = CounterRepository(
            counterDao = database.counterDao(),
            counterEventDao = database.counterEventDao(),
            authRepository = authRepository,
            syncScheduler = syncScheduler
        )

        syncScheduler.schedulePeriodicSync()
    }
}
