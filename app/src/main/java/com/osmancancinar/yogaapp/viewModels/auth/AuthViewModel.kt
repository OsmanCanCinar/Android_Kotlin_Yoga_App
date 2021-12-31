package com.osmancancinar.yogaapp.viewModels.auth

import android.app.Application
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.data.repositories.UserRepositories
import com.osmancancinar.yogaapp.utils.adapters.AuthListener
import com.osmancancinar.yogaapp.viewModels.BaseViewModel
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class AuthViewModel @ViewModelInject constructor(
    @Named("Application") private val app: Application,
    @Named("Repositories") private val repository: UserRepositories
) : BaseViewModel(app,repository)  {

    private val disposables = CompositeDisposable()

    val auth = repository.getFirebaseAuthRef()

    val TAG = "Login"

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