package com.hiashwinsharma.keisuki.data.remote

import com.hiashwinsharma.keisuki.model.ColorToken
import com.hiashwinsharma.keisuki.model.Counter
import com.hiashwinsharma.keisuki.model.SyncStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirestoreService(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private fun userCountersCollection(userId: String) =
        firestore.collection("users").document(userId).collection("counters")

    suspend fun uploadCounter(userId: String, counter: Counter): Result<Unit> {
        return runCatching {
            val docRef = userCountersCollection(userId).document(counter.id)
            val data = mapOf(
                "id" to counter.id,
                "userId" to userId,
                "title" to counter.title,
                "count" to counter.count,
                "step" to counter.step,
                "colorToken" to counter.colorToken.id,
                "createdAt" to counter.createdAt,
                "updatedAt" to counter.updatedAt
            )
            docRef.set(data, SetOptions.merge()).await()
        }
    }

    suspend fun deleteCounter(userId: String, counterId: String): Result<Unit> {
        return runCatching {
            userCountersCollection(userId).document(counterId).delete().await()
        }
    }

    suspend fun fetchCountersUpdatedAfter(userId: String, sinceTimestamp: Long): Result<List<Counter>> {
        return runCatching {
            val snapshot = userCountersCollection(userId)
                .whereGreaterThan("updatedAt", sinceTimestamp)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                val id = doc.getString("id") ?: doc.id
                val title = doc.getString("title") ?: return@mapNotNull null
                val count = doc.getLong("count") ?: 0L
                val step = doc.getLong("step") ?: 1L
                val colorTokenId = doc.getString("colorToken") ?: "electric_rose"
                val updatedAt = doc.getLong("updatedAt") ?: System.currentTimeMillis()
                val createdAt = doc.getLong("createdAt") ?: updatedAt

                Counter(
                    id = id,
                    userId = userId,
                    title = title,
                    count = count,
                    step = step,
                    colorToken = ColorToken.fromId(colorTokenId),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    syncStatus = SyncStatus.SYNCED
                )
            }
        }
    }

    suspend fun fetchAllRemoteCounters(userId: String): Result<List<Counter>> {
        return runCatching {
            val snapshot = userCountersCollection(userId).get().await()
            snapshot.documents.mapNotNull { doc ->
                val id = doc.getString("id") ?: doc.id
                val title = doc.getString("title") ?: return@mapNotNull null
                val count = doc.getLong("count") ?: 0L
                val step = doc.getLong("step") ?: 1L
                val colorTokenId = doc.getString("colorToken") ?: "electric_rose"
                val updatedAt = doc.getLong("updatedAt") ?: System.currentTimeMillis()
                val createdAt = doc.getLong("createdAt") ?: updatedAt

                Counter(
                    id = id,
                    userId = userId,
                    title = title,
                    count = count,
                    step = step,
                    colorToken = ColorToken.fromId(colorTokenId),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    syncStatus = SyncStatus.SYNCED
                )
            }
        }
    }
}
