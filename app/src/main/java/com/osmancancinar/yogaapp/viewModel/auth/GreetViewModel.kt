package com.osmancancinar.yogaapp.viewModel.auth

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.osmancancinar.yogaapp.data.repository.UserRepositories
import com.osmancancinar.yogaapp.viewModel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

class GreetViewModel @ViewModelInject constructor(
    @Named("Application") private val app: Application,
    @Named("Repositories") private val repository: UserRepositories
) : BaseViewModel(app, repository) {

    private val disposables = CompositeDisposable()

    val auth = repository.getFirebaseAuthRef()

    val loginTag = "Login"

    val googleSignInIntent = repository.getGoogleSignInIntent()

    fun authWithGoogle(idToken: String) {

        authListener?.onStarted()
        val disposable = repository.authWithGoogle(idToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { authListener?.onSuccess() },
                { authListener?.onFailure(it.message!!) }
            )
        disposables.add(disposable)
    }

    fun signOutFromGoogle() = repository.signOutWithGoogle()

    var callbackManager: CallbackManager = CallbackManager.Factory.create()

    fun authWithFacebook(token: AccessToken) {

        authListener?.onStarted()

        val disposable = repository.authWithFacebook(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { authListener?.onSuccess() },
                { authListener?.onFailure(it.message!!) })
        disposables.add(disposable)
    }

    fun signOutFromFacebook() {
        LoginManager.getInstance().logOut()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}