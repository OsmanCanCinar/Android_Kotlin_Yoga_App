package com.osmancancinar.yogaapp.viewModels.auth

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.ui.authentication.SignUpFragmentDirections
import com.osmancancinar.yogaapp.ui.home.HomeActivity
import com.osmancancinar.yogaapp.viewModels.BaseViewModel

class SignUpVM(private val app: Application) : BaseViewModel(app) {

    fun navigateToSignIn(view: View) {
        val actionToSignIn = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment3()
        Navigation.findNavController(view).navigate(actionToSignIn)
    }

    fun goBackToOptions(view: View) {
        val actionToOptions = SignUpFragmentDirections.actionGlobalGreetFragment()
        Navigation.findNavController(view).navigate(actionToOptions)
    }

    fun showDialog(context: Context) {
        context.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.terms_and_conditions)
                .setMessage(R.string.terms)
                .setPositiveButton(R.string.dismiss) { _, _ -> }
                .show()
        }
    }

    fun validateEmail(email: String): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return app.getString(R.string.error_msg_email)
        }
        return null
    }

    fun validatePassword(password: String): String? {
        when {
            password.length < 8 -> {
                return app.getString(R.string.error_msg_password)
            }
            !password.matches(".*[A-Z].*".toRegex()) -> {
                return app.getString(R.string.uppercase_error)
            }
            !password.matches(".*[a-z].*".toRegex()) -> {
                return app.getString(R.string.lowercase_error)
            }
            !password.matches(".*[@#\$%^&+].*".toRegex()) -> {
                return app.getString(R.string.special_case_error)
            }
            !password.matches(".*[0-9].*".toRegex()) -> {
                return app.getString(R.string.number_case_error)
            }
            else -> return null
        }
    }

    fun validateName(name: String): String? {
        if (name.length < 3) {
            return app.getString(R.string.error_msg_name_char)
        }
        else if (!name.matches("^[a-zA-Z]*$".toRegex())) {
            return app.getString(R.string.error_msg_name)
        }
        else return null
    }

    fun signUp(activity: Activity) {
        val intent = Intent(activity, HomeActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
}