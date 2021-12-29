package com.osmancancinar.yogaapp.data.repositories

import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.osmancancinar.yogaapp.data.firebase.FirebaseSource

class UserRepositories(private val firebase: FirebaseSource) {

    fun registerUserToDatabase(email: String, username: String) = firebase.registerUserToDatabase(email, username)

    fun authWithFacebook(token: AccessToken) = firebase.handleFacebookAccessToken(token)

    fun authWithGoogle(idToken: String) = firebase.authWithGoogle(idToken)

    fun signUpWithEmail(email: String, username: String, password: String) = firebase.signUpWithEmail(email, username, password)

    fun signInWithEmail(email: String, password: String) = firebase.signInWithEmail(email, password)

    fun getGoogleSignInClient() : GoogleSignInClient = firebase.googleSignInClient

    fun getFirebaseAuthRef() : FirebaseAuth = firebase.firebaseAuth

    fun getGoogleSignInIntent() = firebase.signInIntent // : Intent

    fun signOutFromFirebase() = firebase.signOutFromFirebase()

    fun signOutWithGoogle() = firebase.signOutWithGoogle()
}