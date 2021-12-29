package com.osmancancinar.yogaapp.data.firebase

import android.app.Application
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.osmancancinar.yogaapp.R
import io.reactivex.Completable

class FirebaseSource(val app: Application) {

    val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseFireStore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val gso: GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(app.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

    val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(app, gso)
    }

    val signInIntent = googleSignInClient.signInIntent

    fun registerUserToDatabase(email: String, username: String) =
        Completable.create { emitter ->

            val user = hashMapOf(
                "userEmail" to email,
                "userName" to username,
            )

            firebaseFireStore.collection("users").add(user).addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful)
                        emitter.onComplete()
                    else
                        emitter.onError(it.exception!!)
                }
            }
        }

    fun authWithGoogle(idToken: String) = Completable.create { emitter ->

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (!emitter.isDisposed)
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else
                    emitter.onError(it.exception!!)
        }
    }

    fun handleFacebookAccessToken(token: AccessToken) = Completable.create { emitter ->
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (!emitter.isDisposed) {
                if (task.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(task.exception!!)
            }
        }
    }

    fun signUpWithEmail(email: String, username: String, password: String) =
        Completable.create { emitter ->

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        registerUserToDatabase(email, username)
                        emitter.onComplete()
                    } else
                        emitter.onError(it.exception!!)
                }
            }
        }

    fun signInWithEmail(email: String, password: String) = Completable.create { emitter ->

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun signOutFromFirebase() = firebaseAuth.signOut()

    fun signOutWithGoogle() = googleSignInClient.signOut()

}