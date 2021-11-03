package com.osmancancinar.yogaapp.viewModels.auth

import android.app.Application
import android.util.Patterns
import android.view.View
import androidx.navigation.Navigation
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.ui.authentication.ResetFragmentDirections
import com.osmancancinar.yogaapp.viewModels.BaseViewModel

class ResetVM(private val app: Application) : BaseViewModel(app) {

    fun resetPassword(view: View) {
        //after successfully resetting the password
        val actionToSignIn = ResetFragmentDirections.actionResetFragmentToSignInFragment()
        Navigation.findNavController(view).navigate(actionToSignIn)
    }

    fun validateEmail(email: String): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return app.getString(R.string.error_msg_email)
        }
        return null
    }
}