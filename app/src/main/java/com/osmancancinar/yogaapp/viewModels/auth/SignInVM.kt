package com.osmancancinar.yogaapp.viewModels.auth

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.view.View
import androidx.navigation.Navigation
import com.osmancancinar.yogaapp.ui.authentication.SignInFragmentDirections
import com.osmancancinar.yogaapp.ui.home.HomeActivity
import com.osmancancinar.yogaapp.viewModels.BaseViewModel

class SignInVM(private val app : Application) : BaseViewModel(app) {

    fun navigateToSignUp(view: View) {
        val actionToSignUp = SignInFragmentDirections.actionSignInFragmentToSignUpFragment3()
        Navigation.findNavController(view).navigate(actionToSignUp)
    }

    fun goBackToOptions(view: View) {
        val actionToOptions = SignInFragmentDirections.actionGlobalGreetFragment()
        Navigation.findNavController(view).navigate(actionToOptions)
    }

    fun signIn(activity: Activity) {
        val intent = Intent(activity,HomeActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    fun resetPassword(view: View) {
        val actionToResetPassword = SignInFragmentDirections.actionSignInFragmentToResetFragment()
        Navigation.findNavController(view).navigate(actionToResetPassword)
    }
}
