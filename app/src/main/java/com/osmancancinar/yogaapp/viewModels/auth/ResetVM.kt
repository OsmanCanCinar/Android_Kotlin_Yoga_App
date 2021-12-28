package com.osmancancinar.yogaapp.viewModels.auth

import android.app.Application
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.viewModels.BaseViewModel

class ResetVM(private val app: Application) : BaseViewModel(app) {

    fun validateEmail(email: String): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return app.getString(R.string.error_msg_email)
        }
        return null
    }

    fun resetPassword(email: String, auth: FirebaseAuth) {
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            Toast.makeText(
                app.applicationContext,
                app.getString(R.string.reset_text),
                Toast.LENGTH_SHORT
            ).show()
        }.addOnFailureListener {
            Toast.makeText(
                app.applicationContext,
                it.localizedMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}