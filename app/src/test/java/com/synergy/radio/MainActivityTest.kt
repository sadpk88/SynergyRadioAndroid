package com.synergy.radio

import android.content.Intent
import android.os.Build
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    @Config(sdk = [Build.VERSION_CODES.O])
    fun `startForegroundService on Oreo and above starts foreground service`() {
        val controller = Robolectric.buildActivity(MainActivity::class.java).setup()
        val activity = controller.get()

        val webAppInterface = activity.WebAppInterface()
        webAppInterface.startForegroundService()

        val shadowActivity = shadowOf(activity)
        val startedIntent = shadowActivity.nextStartedService
        assertNotNull(startedIntent)
        assertEquals(RadioService::class.java.name, startedIntent.component?.className)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.N_MR1])
    fun `startForegroundService below Oreo starts normal service`() {
        val controller = Robolectric.buildActivity(MainActivity::class.java).setup()
        val activity = controller.get()

        val webAppInterface = activity.WebAppInterface()
        webAppInterface.startForegroundService()

        val shadowActivity = shadowOf(activity)
        val startedIntent = shadowActivity.nextStartedService
        assertNotNull(startedIntent)
        assertEquals(RadioService::class.java.name, startedIntent.component?.className)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.O])
    fun `stopForegroundService stops the service`() {
        val controller = Robolectric.buildActivity(MainActivity::class.java).setup()
        val activity = controller.get()

        val webAppInterface = activity.WebAppInterface()
        webAppInterface.stopForegroundService()

        val shadowActivity = shadowOf(activity)
        val stoppedIntent = shadowActivity.nextStoppedService
        assertNotNull(stoppedIntent)
        assertEquals(RadioService::class.java.name, stoppedIntent.component?.className)
    }
}
