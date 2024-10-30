package br.concy.demo.health

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import br.concy.demo.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HeartRateService : LifecycleService() {

    private val TAG = "HeartRateService"
    private var isCollecting = true

    @Inject lateinit var healthServicesManager: HealthServicesManager
    @Inject lateinit var repository: PassiveDataRepository

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, createNotification())
        startHeartRateCollection()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent?.action == "STOP_SERVICE") {
            stopHeartRateCollection()
        }
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel for heart rate monitoring"
            enableVibration(true)
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val stopIntent = Intent(this, HeartRateService::class.java).apply {
            action = "STOP_SERVICE"
        }
        val stopPendingIntent: PendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Heart Rate Monitoring")
            .setContentText("Monitoring your heart rate...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.baseline_monitor_heart_24)
            .setOngoing(true)
            .addAction(R.drawable.baseline_stop_circle_24, "Stop", stopPendingIntent)
            .build()
    }

    private fun startHeartRateCollection() {
        lifecycleScope.launch {
            if (healthServicesManager.hasHeartRateCapability()) {
                healthServicesManager.startMeasure()
                    .takeWhile { isCollecting }
                    .collect { message ->
                        when (message) {
                            is MeasureMessage.MeasureData -> {
                                Log.i(TAG, "Received heart rate data: ${message.data.last().value}")
                                repository.storeLatestHeartRate(message.data.last().value)
                            }
                            is MeasureMessage.MeasureAvailability -> {}
                        }
                    }
            }
        }
    }

    private fun stopHeartRateCollection() {
        isCollecting = false
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopHeartRateCollection()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "heart_rate_service_channel"
        private const val CHANNEL_NAME = "Heart Rate Monitoring"
    }
}