package com.osmancancinar.yogaapp.data.repository

import android.content.Intent
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuth
import com.osmancancinar.yogaapp.data.firebase.FirebaseSource
import javax.inject.Inject
import javax.inject.Named

class FirebaseRepositories @Inject constructor(@Named("Source") private val firebase: FirebaseSource) {


    //FIREBASE-FIRESTORE RELATED
    //retrieves data from firebase and saves it to the room to be displayed.
    fun getMeditationCategoriesListFB() = firebase.getMeditationCategoriesListFB()


    fun getMeditationCategoryFB(id: Int) = firebase.getMeditationCategoryFB(id)


    fun getYogaFB(category: String) = firebase.getYogaFB(category)


    //Registers then user who signed up via username-email-password
    fun registerUserToDatabase(email: String, username: String) =
        firebase.registerUserToFirebase(email, username)


    //FIREBASE-AUTH RELATED
    //Signs Out From firebase
    fun signOutFromFirebase() = firebase.signOutFromFirebase()


    //Gets firebaseAuth by lazy
    fun getFirebaseAuthRef(): FirebaseAuth = firebase.firebaseAuth


    //Allows user to sign in with the credentials
    fun signInWithEmail(email: String, password: String) = firebase.signInWithEmail(email, password)


    //Enrolls user to database email-password
    fun signUpWithEmail(email: String, username: String, password: String) =
        firebase.signUpWithEmail(email, username, password)


    //Sends reset link to given email for password
    fun resetPasswordWithEmail(email: String) = firebase.resetPasswordWithEmail(email)


    //Gets gsoClients sign in intent by lazy
    fun getGoogleSignInIntent(): Intent = firebase.signInIntent


    //Authenticates with Google. Both Sign Up and Sign In
    fun authWithGoogle(idToken: String) = firebase.authWithGoogle(idToken)


    //Signs out with gsoClient
    fun signOutWithGoogle() = firebase.signOutWithGoogle()


    //Authenticates with Facebook. Both Sign Up and Sign In
    fun authWithFacebook(token: AccessToken) = firebase.handleFacebookAccessToken(token)
}