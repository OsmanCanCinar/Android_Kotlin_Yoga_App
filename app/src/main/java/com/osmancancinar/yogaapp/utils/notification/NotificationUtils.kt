package com.osmancancinar.yogaapp.utils.notification

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import java.util.*

class NotificationUtils {

    fun setNotification(timeInMilliSeconds: Long, activity: Activity) {
        if (timeInMilliSeconds > 0 ) {

            val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager

            val alarmIntent = Intent(activity.applicationContext, NotificationBR::class.java)

            alarmIntent.putExtra("timeStamp", timeInMilliSeconds)

            var calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMilliSeconds

            calendar = GregorianCalendar.getInstance().apply {
                if (get(Calendar.HOUR_OF_DAY) >= 15) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }

                set(Calendar.HOUR_OF_DAY, 15)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }


            val pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent,PendingIntent.FLAG_CANCEL_CURRENT)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }
}