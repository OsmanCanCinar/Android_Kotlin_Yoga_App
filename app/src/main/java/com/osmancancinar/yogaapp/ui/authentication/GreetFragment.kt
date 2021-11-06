package com.osmancancinar.yogaapp.ui.authentication

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentGreetBinding
import com.osmancancinar.yogaapp.ui.home.HomeActivity
import com.osmancancinar.yogaapp.viewModels.auth.GreetVM

class GreetFragment : Fragment() {

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var binding: FragmentGreetBinding
    private lateinit var viewModel: GreetVM
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        if (auth.currentUser != null) {
            signIn(requireActivity())
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGreetBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GreetVM::class.java)

        binding.buttonEmail.setOnClickListener {
            navigateToEmail(view)
        }

        binding.buttonFacebook.setOnClickListener {
            navigateToFacebook()
        }

        binding.buttonGoogle.setOnClickListener {
            navigateToGoogle()
        }
    }

    private fun navigateToEmail(view: View) {
        val action = GreetFragmentDirections.actionGreetFragmentToSignUpFragment()
        Navigation.findNavController(view).navigate(action)
    }

    private fun navigateToFacebook() {
        Toast.makeText(context, getString(R.string.feature), Toast.LENGTH_SHORT).show()
    }

    private fun navigateToGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signIn(activity: Activity) {
        val intent = Intent(activity, HomeActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!, requireActivity())
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                }
            } else {
                Log.w(TAG, exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, activity: Activity) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {

                val currentUser = auth.currentUser
                val email = currentUser?.email
                val username = currentUser?.displayName

                database.collection("users").whereEqualTo("userEmail", email)
                    .addSnapshotListener { value, e ->
                        if (e != null) {
                            return@addSnapshotListener
                        }

                        val users = ArrayList<String>()

                        for (doc in value!!) {
                            doc.getString("userEmail")?.let {
                                users.add(it)
                            }
                        }

                        if(users.contains(email)) {
                            println("sign in")
                        } else {
                            println("sign up")

                            val user = hashMapOf(
                                "userName" to username,
                                "userEmail" to email
                            )

                            database.collection("users")
                                .add(user)
                                .addOnSuccessListener {
                                    Log.d(TAG, "DocumentSnapshot added with ID: ${it.id}")
                                }
                                .addOnFailureListener {
                                    Log.w(TAG, "Error adding document", it)
                                }
                        }
                    }
            }

            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
            activity.finish()
        }
    }
}