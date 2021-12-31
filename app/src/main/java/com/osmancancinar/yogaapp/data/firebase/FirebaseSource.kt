package com.osmancancinar.yogaapp.data.firebase

import android.app.Application
import android.content.Intent
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
import javax.inject.Inject
import javax.inject.Named

class FirebaseSource @Inject constructor(@Named("Application") val app: Application) {

    //By lazy, we don't initialize firebaseAuth till we use it.
    val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    //User can sign in to our application by using the credentials that they created while signing up.
    fun signInWithEmail(email: String, password: String) =
        Completable.create { emitter ->

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful)
                        emitter.onComplete()
                    else
                        emitter.onError(it.exception!!)
                }
            }
        }

    //User can reset the password by entering their email address. An email will be sent to them.
    fun resetPasswordWithEmail(email: String) = Completable.create { emitter ->

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    //User can sign up to our application by creating an account.
    fun signUpWithEmail(email: String, username: String, password: String) =
        Completable.create { emitter ->

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                    } else
                        emitter.onError(it.exception!!)
                }
            }
        }

    //We sign out from the current firebase user.
    fun signOutFromFirebase() = firebaseAuth.signOut()

    // We initialize google sign in options object to be displayed.
    private val gso: GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(app.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

    //By lazy, we prevent unnecessary object initializations and it is thread safe.
    val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(app, gso)
    }

    //Sign in intent for gsoClient.
    val signInIntent: Intent by lazy {
        googleSignInClient.signInIntent
    }

    //We create request to get a credential with provided token from Google.
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

    //Method above also signs out but in order to avoid bugs we additional sign out from gsoClient too.
    fun signOutWithGoogle() = googleSignInClient.signOut()

    //By lazy, when database variable is reused, we will get it from cache memory.
    private val firebaseFireStore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    //We additionally register the User to the database with email and username.
    fun registerUserToDatabase(email: String, username: String) =
        Completable.create { emitter ->

            val user = hashMapOf(
                "userEmail" to email,
                "userName" to username
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

    //We create request to get a credential with provided token from Facebook.
    fun handleFacebookAccessToken(token: AccessToken) =
        Completable.create { emitter ->

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
}