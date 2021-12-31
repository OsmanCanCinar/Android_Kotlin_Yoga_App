package com.osmancancinar.yogaapp.utils.adapters

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(msg: String)
}