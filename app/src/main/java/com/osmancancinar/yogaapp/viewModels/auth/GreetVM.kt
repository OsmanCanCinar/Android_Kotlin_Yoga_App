package com.osmancancinar.yogaapp.viewModels.auth

import android.app.Application
import android.view.View
import androidx.navigation.Navigation
import com.osmancancinar.yogaapp.ui.authentication.GreetFragmentDirections
import com.osmancancinar.yogaapp.viewModels.BaseViewModel

class GreetVM(private val app : Application) : BaseViewModel(app) {

    fun navigateToEmail(view : View) {
        val action = GreetFragmentDirections.actionGreetFragmentToSignUpFragment()
        Navigation.findNavController(view).navigate(action)
    }
}