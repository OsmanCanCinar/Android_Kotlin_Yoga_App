package com.osmancancinar.yogaapp.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationBR : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {

        val service = Intent(context, NotificationService::class.java)
        service.putExtra("timeStamp",intent.getLongExtra("timeStamp",0))
        context.startService(service)
    }
}