package com.osmancancinar.yogaapp.ui.authentication

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.osmancancinar.yogaapp.data.repositories.UserRepositories
import com.osmancancinar.yogaapp.viewModels.auth.AuthViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(
    private val app: Application,
    private val repository: UserRepositories
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java))
            return AuthViewModel(app, repository) as T
        else
            throw IllegalArgumentException("Unknown ViewModel Class")
    }
}