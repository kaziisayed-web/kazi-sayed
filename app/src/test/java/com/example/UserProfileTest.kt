package com.example

import com.example.data.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UserProfileTest {

    @Test
    fun testUserProfileDefaults() {
        val profile = UserProfile(
            userId = "user_123",
            displayName = "Kazi Sayed",
            email = "kaziisayed@gmail.com",
            bio = "Loving MONKHULE community",
            avatar = "🦊"
        )

        assertEquals("user_123", profile.userId)
        assertEquals("Kazi Sayed", profile.displayName)
        assertEquals("kaziisayed@gmail.com", profile.email)
        assertEquals("Loving MONKHULE community", profile.bio)
        assertEquals("🦊", profile.avatar)
        assertEquals("user", profile.role)
    }

    @Test
    fun testUserProfileUpdateCopy() {
        val initial = UserProfile(
            userId = "uid_456",
            displayName = "Old Name",
            bio = "Old Bio",
            avatar = "👤"
        )

        val updated = initial.copy(
            displayName = "New Name",
            bio = "Updated Bio with inspirational thoughts",
            avatar = "🚀"
        )

        assertEquals("uid_456", updated.userId)
        assertEquals("New Name", updated.displayName)
        assertEquals("Updated Bio with inspirational thoughts", updated.bio)
        assertEquals("🚀", updated.avatar)
    }
}
