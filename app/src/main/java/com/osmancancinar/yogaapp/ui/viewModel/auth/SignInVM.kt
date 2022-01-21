package com.osmancancinar.yogaapp.ui.viewModel.auth

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.osmancancinar.yogaapp.data.repository.FirebaseRepositories
import com.osmancancinar.yogaapp.ui.viewModel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

class SignInVM @ViewModelInject constructor(
    @Named("Application") private val app: Application,
    @Named("Repositories") private val repository: FirebaseRepositories
) : BaseViewModel(app, repository) {

    private val disposables = CompositeDisposable()

    fun validateEmail(email: String?): String? {

        return if (email.isNullOrEmpty()) {
            "Email cannot be empty"
        } else
            null
    }

    fun validatePassword(password: String?): String? {

        return if (password.isNullOrEmpty()) {
            "Password cannot be empty"
        } else
            null
    }

    private fun validateSignInInput(email: String?, password: String?) {

        validateEmail(email)
        validatePassword(password)

    }

    fun signInWithEmail(email: String?, password: String?) {

        validateSignInInput(email, password)

        firebaseListener?.onStarted()

        val disposable = repository.signInWithEmail(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { firebaseListener?.onSuccess() },
                { firebaseListener?.onFailure(it.message!!) }
            )
        disposables.add(disposable)

    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}