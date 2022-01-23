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
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.data.database.AppDatabase
import com.osmancancinar.yogaapp.data.model.MeditationCategoriesList
import com.osmancancinar.yogaapp.data.model.MeditationCategory
import com.osmancancinar.yogaapp.data.model.Yoga
import com.osmancancinar.yogaapp.data.repository.RoomRepositories
import io.reactivex.Completable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class FirebaseSource @Inject constructor(
    @Named("Application") val app: Application
) // :CoroutineScope {
{

    /*
                                            FIREBASE-AUTH RELATED

        We are using Completable that is an RxJava class. Completable basically means it holds something that
        will complete and we can get an indication when it is completed or failed. And it is the perfect class
        to use with FirebaseAuth because auth is a network operation that will complete and we need to know if
        it is completed or failed.

        So here we are creating a Completable and inside the completable we are performing the authentication
        and firebase reads-writes. Once it is completed we are using the emitter to indicated that the task is
        completed or failed.

        We are also using lazy to create only one instance of some of our val values to be accessed from cache memory.
     */


    //By lazy, we don't initialize firebaseAuth till we use it.
    val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    // We initialize google sign in options object to be displayed.
    private val gso: GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(app.getString(R.string.default_web_client_id)).requestEmail().build()


    //By lazy, we prevent unnecessary object initializations and it is thread safe.
    private val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(app, gso)
    }


    //Sign in intent for gsoClient.
    val signInIntent: Intent by lazy {
        googleSignInClient.signInIntent
    }


    //We sign out from the current firebase user.
    fun signOutFromFirebase() = firebaseAuth.signOut()


    //Method above also signs out but in order to avoid bugs we additional sign out from gsoClient too.
    fun signOutWithGoogle() = googleSignInClient.signOut()


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


    //FIREBASE-FIRESTORE RELATED

    //By lazy, when database variable is reused, we will get it from cache memory.
    private val firebaseFireStore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }


    //For coroutines we declare and initialize a job.
    //private val job = Job()


    //The Input/Output operations(Firebase & Room) must be performed on the IO Thread. !NOT THE MAIN(UI) THREAD!
    //override val coroutineContext: CoroutineContext
    // get() = job + Dispatchers.IO


    //Instance of our Database Access Object to perform insertion of category list from firestore.
    private val dao = AppDatabase.invoke(app.applicationContext).appDao()


    //We call queries via a repository not directly.
    private val roomRepositories = RoomRepositories(dao)


    //After retrieving the list from firestore, in a coroutine we clean and insert data to Room Database.
    fun getMeditationCategoriesListFB() = Completable.create { emitter ->
        val meditationCategoriesList: ArrayList<MeditationCategoriesList> = arrayListOf()
        firebaseFireStore.collection("meditation_categories")
            .orderBy("categoryId", Query.Direction.ASCENDING).get()
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        for (dc: DocumentChange in it.result?.documentChanges!!) {
                            if (dc.type == DocumentChange.Type.ADDED) {
                                meditationCategoriesList.add(
                                    dc.document.toObject(
                                        MeditationCategoriesList::class.java
                                    )
                                )
                            }
                        }
                        GlobalScope.launch(Dispatchers.IO) {
                            roomRepositories.deleteMeditationCategoriesList()
                            roomRepositories.insertMeditationCategoriesList(meditationCategoriesList)
                        }
                        emitter.onComplete()
                    } else
                        emitter.onError(it.exception!!)
                }
            }
    }


    fun getMeditationCategoryFB(id: Int) = Completable.create { emitter ->
        val meditationCategoryList: ArrayList<MeditationCategory> = arrayListOf()
        firebaseFireStore.collection("meditation_categories").document(id.toString())
            .collection("meditations")
            .orderBy("meditationId", Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        for (dc: DocumentChange in it.result?.documentChanges!!) {
                            if (dc.type == DocumentChange.Type.ADDED) {
                                meditationCategoryList.add(dc.document.toObject(MeditationCategory::class.java))
                            }
                        }
                        GlobalScope.launch(Dispatchers.IO) {
                            roomRepositories.deleteMeditationCategory()
                            roomRepositories.insertMeditationCategory(meditationCategoryList)
                        }
                        emitter.onComplete()
                    } else
                        emitter.onError(it.exception!!)
                }
            }
    }


    // .orderBy("yogaId", Query.Direction.ASCENDING)
    fun getYogaFB(category: String) = Completable.create { emitter ->
        val yogaList: ArrayList<Yoga> = arrayListOf()
        firebaseFireStore.collection("yoga")
            .whereEqualTo("category", category)
            .get()
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        for (dc: DocumentChange in it.result?.documentChanges!!) {
                            if (dc.type == DocumentChange.Type.ADDED) {
                                yogaList.add(dc.document.toObject(Yoga::class.java))
                            }
                        }
                        GlobalScope.launch(Dispatchers.IO) {
                            roomRepositories.deleteYogaList()
                            roomRepositories.insertYogaList(yogaList)
                        }
                        emitter.onComplete()
                    } else
                        emitter.onError(it.exception!!)
                }
            }
    }


    //We additionally register the User to the database with email and username.
    fun registerUserToFirebase(email: String, username: String) =
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


}