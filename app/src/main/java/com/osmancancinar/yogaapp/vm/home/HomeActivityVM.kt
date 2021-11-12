package com.osmancancinar.yogaapp.vm.home

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.ui.home.HomeActivity
import com.osmancancinar.yogaapp.vm.BaseViewModel

class HomeActivityVM(private val app: Application) : BaseViewModel(app) {

    companion object {
        const val CHANNEL_ID = "channelID"
        const val CHANNEL_NAME = "YogaApp"
        const val NOTIFICATION_ID = 0
        const val titleExtra = "titleExtra"
        const val messageExtra = "messageExtra"
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Companion.CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun sendNotification() {
        val intent = Intent(app, HomeActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(app).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(app.applicationContext, Companion.CHANNEL_ID)
            .setContentTitle(titleExtra) //app.getString(R.string.notification_tip)
            .setContentText(messageExtra)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageExtra))
            .build()

        val notificationManager = NotificationManagerCompat.from(app.applicationContext)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}