package com.osmancancinar.yogaapp.ui.authentication

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(msg: String)
}