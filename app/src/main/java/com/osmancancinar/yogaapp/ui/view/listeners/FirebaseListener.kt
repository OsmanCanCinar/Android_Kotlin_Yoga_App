package com.osmancancinar.yogaapp.ui.view.listeners

interface FirebaseListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(msg: String)
}