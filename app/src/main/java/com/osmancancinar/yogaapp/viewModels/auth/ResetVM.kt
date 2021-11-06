package com.osmancancinar.yogaapp.viewModels.auth

import android.app.Application
import android.util.Patterns
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.viewModels.BaseViewModel

class ResetVM(private val app: Application) : BaseViewModel(app) {

    fun validateEmail(email: String): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return app.getString(R.string.error_msg_email)
        }
        return null
    }
}