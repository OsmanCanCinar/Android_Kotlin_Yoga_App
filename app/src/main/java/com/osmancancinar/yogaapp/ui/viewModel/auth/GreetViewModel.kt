package com.osmancancinar.yogaapp.ui.viewModel.auth

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.osmancancinar.yogaapp.data.repository.FirebaseRepositories
import com.osmancancinar.yogaapp.ui.viewModel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

class GreetViewModel @ViewModelInject constructor(
    @Named("Application") private val app: Application,
    @Named("Repositories") private val repository: FirebaseRepositories
) : BaseViewModel(app, repository) {

    private val disposables = CompositeDisposable()

    val callbackManager: CallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    //var callbackManager: CallbackManager = CallbackManager.Factory.create()

    val auth = repository.getFirebaseAuthRef()

    val loginTag = "Login"

    val googleSignInIntent = repository.getGoogleSignInIntent()

    fun authWithGoogle(idToken: String) {
        //maybe launch here?
        firebaseListener?.onStarted()
        val disposable = repository.authWithGoogle(idToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { firebaseListener?.onSuccess() },
                { firebaseListener?.onFailure(it.message!!) }
            )
        disposables.add(disposable)
    }

    fun signOutFromGoogle() = repository.signOutWithGoogle()

    fun authWithFacebook(token: AccessToken) {

        firebaseListener?.onStarted()

        val disposable = repository.authWithFacebook(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { firebaseListener?.onSuccess() },
                { firebaseListener?.onFailure(it.message!!) })
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