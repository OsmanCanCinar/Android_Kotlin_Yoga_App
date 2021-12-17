package com.osmancancinar.yogaapp.ui.authentication

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentGreetBinding
import com.osmancancinar.yogaapp.ui.home.HomeActivity
import com.osmancancinar.yogaapp.vm.auth.GreetVM

class GreetFragment : Fragment() {

    companion object {
        private const val TAG = "Login"
    }

    private lateinit var binding: FragmentGreetBinding
    private lateinit var viewModel: GreetVM
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private var email: String? = null
    private var name: String? = null
    private var imgUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        callbackManager = CallbackManager.Factory.create()

        if (auth.currentUser != null) {
            signIn()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
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

        termsSpan()

        binding.buttonEmail.setOnClickListener {
            navigateToEmail(view)
        }

        binding.loginButton.setOnClickListener {
            navigateToFacebook()
        }

        binding.buttonFacebook.setOnClickListener {
            binding.loginButton.performClick()
        }

        binding.buttonGoogle.setOnClickListener { _ ->
            signInWithGoogle()
        }
    }

    private fun termsSpan() {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.terms_and_conditions)
                    .setMessage(R.string.terms)
                    .setPositiveButton(R.string.dismiss) { _, _ -> }
                    .show()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.reddish_color)
                ds.bgColor = ContextCompat.getColor(requireContext(), R.color.lightest_color)
            }
        }

        val clickableSpan2 = object : ClickableSpan() {
            override fun onClick(widget: View) {
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.privacy_policy)
                    .setMessage(R.string.privacy)
                    .setPositiveButton(R.string.dismiss) { _, _ -> }
                    .show()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.reddish_color)
                ds.bgColor = ContextCompat.getColor(requireContext(), R.color.lightest_color)
            }
        }

        val spannableString = SpannableString(getString(R.string.terms_text))

        spannableString.setSpan(clickableSpan,32,52,Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickableSpan2,57,71,Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

        binding.termsAndConditionsText.text = spannableString
        binding.termsAndConditionsText.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun navigateToEmail(view: View) {
        val action = GreetFragmentDirections.actionGreetFragmentToSignUpFragment()
        Navigation.findNavController(view).navigate(action)
    }

    private fun navigateToFacebook() {
        LoginManager.getInstance().logOut()
        binding.loginButton.setPermissions("email", "public_profile")
        binding.loginButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(TAG, "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG, "facebook:onError", error)
                }
            })
    }

    private fun signIn() {
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun signInWithGoogle() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                }
            }
        }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    email = user?.email
                    name = user?.displayName
                    imgUrl = user?.photoUrl.toString()
                    viewModel.addToDb(email, name, imgUrl!!, database)
                    signIn()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    email = currentUser?.email
                    name = currentUser?.displayName
                    imgUrl = currentUser?.photoUrl.toString()
                    viewModel.addToDb(email, name, imgUrl!!, database)
                    signIn()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }
}