package com.osmancancinar.yogaapp.viewModels.home

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.data.repositories.UserRepositories
import com.osmancancinar.yogaapp.ui.home.HomeActivity
import com.osmancancinar.yogaapp.utils.Constants.CHANNEL_ID
import com.osmancancinar.yogaapp.utils.Constants.CHANNEL_NAME
import com.osmancancinar.yogaapp.utils.Constants.NOTIFICATION_ID
import com.osmancancinar.yogaapp.utils.Constants.messageExtra
import com.osmancancinar.yogaapp.utils.Constants.titleExtra
import com.osmancancinar.yogaapp.viewModels.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Named

class HomeActivityVM(private val app: Application) : AndroidViewModel(app)  {
    //@ViewModelInject constructor( @Named("Application") private val app: Application) : AndroidViewModel(app)

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
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

        val notification = NotificationCompat.Builder(app.applicationContext, CHANNEL_ID)
            .setContentTitle(titleExtra)
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