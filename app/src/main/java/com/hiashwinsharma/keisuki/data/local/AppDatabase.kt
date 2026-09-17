package com.hiashwinsharma.keisuki.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [CounterEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(SyncStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun counterDao(): CounterDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "keisuki_counters.db"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        db.execSQL("UPDATE counters SET createdAt = updatedAt WHERE createdAt = 0 OR createdAt IS NULL")
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
