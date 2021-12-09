package com.osmancancinar.yogaapp.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.ui.authentication.AuthActivity
import java.util.*

class NotificationService : IntentService("NotificationService") {

    private lateinit var notification: Notification
    private val id: Int = 1000

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val context = this.applicationContext
            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH

            val notificationChannel = NotificationChannel(Constants.CHANNEL_ID,Constants.CHANNEL_NAME,importance)

            notificationChannel.apply {
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
                lightColor = Color.parseColor("#e8334a")
                description = Constants.messageExtra
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        createNotificationChannel()

        var timeStamp: Long = 0

        if (intent != null && intent.extras != null) {
            timeStamp = intent.extras!!.getLong("timeStamp")
        }

        if (timeStamp > 0) {
            val context = this.applicationContext

            var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notifyIntent = Intent(this,AuthActivity::class.java)
            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeStamp

            val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val res = this.resources
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                notification = Notification.Builder(this,Constants.CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setLargeIcon(BitmapFactory.decodeResource(res,R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setContentTitle(Constants.titleExtra)
                    .setStyle(Notification.BigTextStyle().bigText(Constants.messageExtra))
                    .setContentText(Constants.messageExtra)
                    .build()
            } else {

                notification = Notification.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle(Constants.titleExtra)
                    .setStyle(Notification.BigTextStyle().bigText(Constants.messageExtra))
                    .setSound(uri)
                    .setContentText(Constants.messageExtra)
                    .build()
            }

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(id,notification)
        }
    }
}