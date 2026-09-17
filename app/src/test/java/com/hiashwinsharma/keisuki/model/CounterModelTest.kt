package com.hiashwinsharma.keisuki.model

import com.hiashwinsharma.keisuki.data.local.CounterEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class CounterModelTest {

    @Test
    fun testDomainToEntityAndBack() {
        val domain = Counter(
            id = "test-uuid-1",
            userId = "user_123",
            title = "Pushups",
            count = 42L,
            step = 5L,
            colorToken = ColorToken.ELECTRIC_ROSE,
            createdAt = 1699999999000L,
            updatedAt = 1700000000000L,
            syncStatus = SyncStatus.PENDING_SYNC
        )

        val entity = CounterEntity.fromDomain(domain)
        assertEquals("test-uuid-1", entity.id)
        assertEquals("user_123", entity.userId)
        assertEquals("Pushups", entity.title)
        assertEquals(42L, entity.count)
        assertEquals(5L, entity.step)
        assertEquals("electric_rose", entity.colorToken)
        assertEquals(1699999999000L, entity.createdAt)
        assertEquals(1700000000000L, entity.updatedAt)
        assertEquals(SyncStatus.PENDING_SYNC, entity.syncStatus)

        val convertedDomain = entity.toDomain()
        assertEquals(domain, convertedDomain)
    }

    @Test
    fun testColorTokenLookup() {
        assertEquals(ColorToken.DYNAMIC_PRIMARY, ColorToken.fromId("dynamic_primary"))
        assertEquals(ColorToken.DYNAMIC_TERTIARY, ColorToken.fromId("dynamic_tertiary"))
        assertEquals(ColorToken.ELECTRIC_ROSE, ColorToken.fromId("electric_rose"))
        assertEquals(ColorToken.NEON_JADE, ColorToken.fromId("neon_jade"))
        assertEquals(ColorToken.CYBER_CORAL, ColorToken.fromId("cyber_coral"))
        assertEquals(ColorToken.DYNAMIC_PRIMARY, ColorToken.fromId("unknown_token"))
    }

    @Test
    fun testColorTokenAutoCycling() {
        assertEquals(ColorToken.DYNAMIC_TERTIARY, ColorToken.DYNAMIC_PRIMARY.nextColor())
        assertEquals(ColorToken.DYNAMIC_ACCENT, ColorToken.DYNAMIC_TERTIARY.nextColor())
        assertEquals(ColorToken.DYNAMIC_SECONDARY, ColorToken.DYNAMIC_ACCENT.nextColor())
        assertEquals(ColorToken.DYNAMIC_PRIMARY, ColorToken.DYNAMIC_SECONDARY.nextColor())
        // When last counter was a fixed custom token, cycle returns first dynamic token
        assertEquals(ColorToken.DYNAMIC_PRIMARY, ColorToken.ELECTRIC_ROSE.nextColor())
        assertEquals(ColorToken.DYNAMIC_PRIMARY, ColorToken.entries.last().nextColor())
    }

    @Test
    fun testCounterSorting() {
        val c1 = Counter(id = "1", title = "Zebra", count = 10, createdAt = 100L, updatedAt = 300L)
        val c2 = Counter(id = "2", title = "Apple", count = 50, createdAt = 300L, updatedAt = 100L)
        val c3 = Counter(id = "3", title = "Banana", count = 30, createdAt = 200L, updatedAt = 200L)
        val list = listOf(c1, c2, c3)

        // Latest Change (updatedAt DESC)
        val byLatestChange = list.sortedByDescending { it.updatedAt }
        assertEquals(listOf("1", "3", "2"), byLatestChange.map { it.id })

        // Creation Date (createdAt DESC)
        val byCreationDate = list.sortedByDescending { it.createdAt }
        assertEquals(listOf("2", "3", "1"), byCreationDate.map { it.id })

        // Name (A-Z case insensitive)
        val byName = list.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.title })
        assertEquals(listOf("2", "3", "1"), byName.map { it.id })

        // Highest Count (count DESC)
        val byCount = list.sortedByDescending { it.count }
        assertEquals(listOf("2", "3", "1"), byCount.map { it.id })
    }

    @Test
    fun testLegacyCreatedAtFallbackAndPreservation() {
        // Entity with 0 createdAt falls back to updatedAt
        val legacyEntity = CounterEntity(
            id = "legacy-1",
            userId = null,
            title = "Legacy",
            count = 10L,
            step = 1L,
            colorToken = "dynamic_primary",
            createdAt = 0L,
            updatedAt = 1600000000000L,
            syncStatus = SyncStatus.SYNCED
        )
        val domain = legacyEntity.toDomain()
        assertEquals(1600000000000L, domain.createdAt)

        // When domain is converted back to entity, createdAt is locked in
        val entityFromDomain = CounterEntity.fromDomain(domain)
        assertEquals(1600000000000L, entityFromDomain.createdAt)

        // When counter is later updated (e.g. incremented, updatedAt advances), createdAt remains unchanged
        val updatedCounter = domain.copy(updatedAt = 1700000000000L)
        val updatedEntity = CounterEntity.fromDomain(updatedCounter)
        assertEquals(1600000000000L, updatedEntity.createdAt)
        assertEquals(1700000000000L, updatedEntity.updatedAt)
    }
}
