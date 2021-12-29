package com.osmancancinar.yogaapp.viewModels.auth

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.osmancancinar.yogaapp.data.repositories.UserRepositories
import com.osmancancinar.yogaapp.ui.authentication.AuthListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthViewModel(
    private val app: Application,
    private val repository: UserRepositories
) : AndroidViewModel(app) {

    var authListener: AuthListener? = null
    var googleSignInClient: GoogleSignInClient = repository.getGoogleSignInClient()
    var callbackManager: CallbackManager = CallbackManager.Factory.create()

    private val disposables = CompositeDisposable()

    val TAG = "Login"

    val auth = repository.getFirebaseAuthRef()

    val googleSignInIntent = repository.getGoogleSignInIntent()

    fun registerUser(email: String, username: String) =
        repository.registerUserToDatabase(email, username)

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

    fun signInWithEmail(email: String?, password: String?) {

        validateSignInInput(email, password)

        authListener?.onStarted()

        val disposable = repository.signInWithEmail(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { authListener?.onSuccess() },
                { authListener?.onFailure(it.message!!) }
            )
        disposables.add(disposable)

    }

    fun signUpWithEmail(email: String?, username: String?, password: String?) {

        validateSignUpInput(email, username, password)

        authListener?.onStarted()

        val disposable = repository.signUpWithEmail(email!!, username!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { authListener?.onSuccess() },
                { authListener?.onFailure(it.message!!) }
            )
        disposables.add(disposable)

    }

    private fun validateEmail(email: String?) {

        if (email.isNullOrEmpty()) {
            authListener?.onFailure("Email cannot be empty")
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            authListener?.onFailure("Invalid Email Address")
            return
        }

    }

    private fun validateUsername(username: String?) {

        if (username.isNullOrEmpty()) {
            authListener?.onFailure("Username cannot be empty")
            return
        } else if (username!!.length < 3) {
            authListener?.onFailure("Username must contain at least 3 characters")
            return
        } else if (!username!!.matches("^[a-zA-Z]*$".toRegex())) {
            authListener?.onFailure("Username can only consist of characters")
            return
        }

    }

    private fun validatePassword(password: String?) {

        when {
            password.isNullOrEmpty() -> {
                authListener?.onFailure("Password cannot be empty")
                return
            }
            password!!.length < 8 -> {
                authListener?.onFailure("Password must include at least 8 characters")
                return
            }
            !password!!.matches(".*[A-Z].*".toRegex()) -> {
                authListener?.onFailure("Password must contain at least 1 Upper-case character")
                return
            }
            !password!!.matches(".*[a-z].*".toRegex()) -> {
                authListener?.onFailure("Password must contain at least 1 Lower-case character")
                return
            }
            !password!!.matches(".*[@#\$%^&+].*".toRegex()) -> {
                authListener?.onFailure("Password must contain at least 1 Special-case character (@#\$%^&amp;+)")
                return
            }
            !password!!.matches(".*[0-9].*".toRegex()) -> {
                authListener?.onFailure("Password must contain at least 1 number (0–9)")
                return
            }
            else -> return
        }

    }

    private fun validateSignUpInput(email: String?, username: String?, password: String?) {

        validateEmail(email)
        validateUsername(username)
        validatePassword(password)

    }

    private fun validateSignInInput(email: String?, password: String?) {

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Incorrect Email or Password")
            return
        }

    }

    override fun onCleared() {

        super.onCleared()
        disposables.dispose()

    }

}