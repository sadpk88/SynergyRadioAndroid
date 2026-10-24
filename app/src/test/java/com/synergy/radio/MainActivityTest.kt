package com.synergy.radio

import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    fun stopForegroundService_stopsRadioService() {
        // Arrange
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        val webAppInterface = activity.WebAppInterface()

        // Act
        webAppInterface.stopForegroundService()

        // Assert
        val shadowActivity = Shadows.shadowOf(activity)
        val stoppedIntent = shadowActivity.nextStoppedService

        assertNotNull("Expected a service to be stopped", stoppedIntent)
        assertEquals(RadioService::class.java.name, stoppedIntent?.component?.className)
    }
}
