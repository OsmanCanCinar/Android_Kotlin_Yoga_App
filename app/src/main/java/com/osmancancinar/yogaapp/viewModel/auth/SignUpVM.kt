package com.osmancancinar.yogaapp.viewModel.auth

import android.app.Application
import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import com.osmancancinar.yogaapp.data.repository.UserRepositories
import com.osmancancinar.yogaapp.viewModel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

class SignUpVM @ViewModelInject constructor(
    @Named("Application") private val app: Application,
    @Named("Repositories") private val repository: UserRepositories
) : BaseViewModel(app, repository) {

    private val disposables = CompositeDisposable()

    var errorMessage: String = ""

    fun registerUser(email: String, username: String) {

        authListener?.onStarted()
        val disposable = repository.registerUserToDatabase(email, username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { authListener?.onSuccess() },
                { authListener?.onFailure(it.message!!) }) //println(it.message!!)
        disposables.add(disposable)

    }

    fun signUpWithEmail(email: String?, username: String?, password: String?) {

        validateSignUpInput(email, username, password)

        authListener?.onStarted()

        val disposable = repository.signUpWithEmail(email!!, username!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { registerUser(email, username) },
                { authListener?.onFailure(it.message!!) }
            )
        disposables.add(disposable)

    }

    fun validateEmail(email: String?): String? {

        errorMessage = if (email.isNullOrEmpty()) {
            "Email cannot be empty"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Invalid Email Address"
        } else
            return null

        return errorMessage
    }

    fun validateUsername(username: String?): String? {

        errorMessage =
            if (username.isNullOrEmpty()) {
                "Username cannot be empty"
            } else if (username.length < 3) {
                "Username must contain at least 3 characters"
            } else if (!username.matches("^[a-zA-Z]*$".toRegex())) {
                "Username can only consist of characters"
            } else {
                return null
            }

        return errorMessage
    }

    fun validatePassword(password: String?): String? {

        when {
            password.isNullOrEmpty() -> {
                errorMessage = "Password cannot be empty"
            }
            password.length < 8 -> {
                errorMessage = "Password must include at least 8 characters"
            }
            !password.matches(".*[A-Z].*".toRegex()) -> {
                errorMessage = "Password must contain at least 1 Upper-case character"
            }
            !password.matches(".*[a-z].*".toRegex()) -> {
                errorMessage = "Password must contain at least 1 Lower-case character"
            }
            !password.matches(".*[@#\$%^&+].*".toRegex()) -> {
                errorMessage =
                    "Password must contain at least 1 Special-case character (@#\$%^&amp;+)"
            }
            !password.matches(".*[0-9].*".toRegex()) -> {
                errorMessage = "Password must contain at least 1 number (0–9)"
            }
            else -> return null
        }

        return errorMessage
    }

    private fun validateSignUpInput(email: String?, username: String?, password: String?) {

        validateEmail(email)
        validateUsername(username)
        validatePassword(password)

    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}