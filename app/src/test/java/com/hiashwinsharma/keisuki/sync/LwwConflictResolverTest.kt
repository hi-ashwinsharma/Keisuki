package com.hiashwinsharma.keisuki.sync

import com.hiashwinsharma.keisuki.model.Counter
import com.hiashwinsharma.keisuki.model.SyncStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class LwwConflictResolverTest {

    private fun resolveLww(local: Counter, remote: Counter): Counter {
        return if (remote.updatedAt > local.updatedAt) {
            remote.copy(syncStatus = SyncStatus.SYNCED)
        } else {
            local.copy(syncStatus = SyncStatus.SYNCED)
        }
    }

    @Test
    fun testRemoteWinsWhenTimestampIsNewer() {
        val local = Counter(
            id = "c1",
            title = "Local Title",
            count = 10L,
            updatedAt = 1000L,
            syncStatus = SyncStatus.PENDING_SYNC
        )
        val remote = Counter(
            id = "c1",
            title = "Remote Title",
            count = 15L,
            updatedAt = 2000L,
            syncStatus = SyncStatus.SYNCED
        )

        val resolved = resolveLww(local, remote)
        assertEquals(15L, resolved.count)
        assertEquals("Remote Title", resolved.title)
        assertEquals(SyncStatus.SYNCED, resolved.syncStatus)
    }

    @Test
    fun testLocalWinsWhenTimestampIsNewer() {
        val local = Counter(
            id = "c1",
            title = "Local Title",
            count = 25L,
            updatedAt = 3000L,
            syncStatus = SyncStatus.PENDING_SYNC
        )
        val remote = Counter(
            id = "c1",
            title = "Remote Title",
            count = 15L,
            updatedAt = 2000L,
            syncStatus = SyncStatus.SYNCED
        )

        val resolved = resolveLww(local, remote)
        assertEquals(25L, resolved.count)
        assertEquals("Local Title", resolved.title)
        assertEquals(SyncStatus.SYNCED, resolved.syncStatus)
    }
}
