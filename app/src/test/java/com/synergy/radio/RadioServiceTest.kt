package com.synergy.radio

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O])
class RadioServiceTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
    }

    @Test
    fun testOnCreate_createsNotificationChannel() {
        val controller = Robolectric.buildService(RadioService::class.java)
        controller.create()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = notificationManager.getNotificationChannel(RadioService.CHANNEL_ID)

        assertNotNull(channel)
        assertEquals("Radio Playback Channel", channel.name)
    }

    @Test
    fun testOnStartCommand_startsForegroundService() {
        val controller = Robolectric.buildService(RadioService::class.java)
        controller.create()

        controller.startCommand(0, 1)

        val service = controller.get()
        val shadowService = shadowOf(service)
        val notification = shadowService.lastForegroundNotification

        assertNotNull("Foreground notification should not be null", notification)
        assertEquals("Notification ID should match", RadioService.NOTIFICATION_ID, shadowService.lastForegroundNotificationId)
    }

    @Test
    fun testOnBind_returnsNull() {
        val controller = Robolectric.buildService(RadioService::class.java)
        controller.create()
        val service = controller.get()

        val intent = Intent(context, RadioService::class.java)
        val binder = service.onBind(intent)

        assertNull(binder)
    }

    @Test
    fun testOnDestroy_doesNotCrash() {
        val controller = Robolectric.buildService(RadioService::class.java)
        controller.create().destroy()
    }
}
