package com.locknotify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotifyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "锁屏提醒"
        val text  = intent.getStringExtra("text")  ?: ""

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "lock_alert"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(
                channelId, "锁屏时长提醒", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "亮屏时提示上次锁屏时长"
                enableVibration(true)
                setShowBadge(true)
            }
            nm.createNotificationChannel(ch)
        }

        val n = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(1001, n)
        } catch (_: SecurityException) { }
    }
}
