package com.dreamist.foldablerotation

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoldableStateService : LifecycleService() {

    private val deviceStateManager by lazy { DeviceStateManagerWrapper(this) }

    // 设置通知频道
    companion object {
        private const val CHANNEL_ID = "FoldableStateServiceChannel"
        private const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        // 服务启动后执行事件循环
        lifecycleScope.launch {
            deviceStateManager.startListening { isFolded ->
                lifecycleScope.launch {
                    setAutoRotationEnabled(!isFolded)
                }
            }
        }
    }

    // 设置通知内容、启动前台服务
    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {
        // 点击通知后跳转到主界面
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Foldable Rotation Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.service_notification_title))
            .setContentText(getString(R.string.service_notification_content))
            .setSmallIcon(R.drawable.ic_notification) // 替换为实际的图标资源
            .setContentIntent(pendingIntent) // 设置点击行为
            .build()

        startForeground(NOTIFICATION_ID, notification)
        Log.d("FoldableStateService", "Service started")
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch {
            deviceStateManager.stopListening()
        }
        stopForeground(true)
        Log.d("FoldableStateService", "Service stopped")
    }

    // 设置自动旋转
    private suspend fun setAutoRotationEnabled(enabled: Boolean) {
        // 检查切换旋转锁定开关
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isAutoRotationEnabled = sharedPref.getBoolean("auto_rotation", false)
        if(!isAutoRotationEnabled)
            return
        withContext(Dispatchers.IO) {
            val success = Settings.System.putInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                if (enabled) 1 else 0
            )
            Log.d("FoldableStateService", "Auto-rotate set to: $enabled, result: $success")
        }
    }
}