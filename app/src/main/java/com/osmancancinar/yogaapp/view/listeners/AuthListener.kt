package com.osmancancinar.yogaapp.view.listeners

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(msg: String)
}