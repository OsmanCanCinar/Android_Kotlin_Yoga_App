package com.osmancancinar.yogaapp.ui.view.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentGreetBinding
import com.osmancancinar.yogaapp.ui.view.home.HomeActivity
import com.osmancancinar.yogaapp.ui.view.listeners.FirebaseListener
import com.osmancancinar.yogaapp.ui.viewModel.auth.GreetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GreetFragment : Fragment(), FirebaseListener {

    private lateinit var binding: FragmentGreetBinding
    private var currentUser: FirebaseUser? = null
    private val viewModel: GreetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.firebaseListener = this

        if (viewModel.auth.currentUser != null) {
            currentUser = viewModel.auth.currentUser
            signIn()
        }
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

        binding.buttonEmail.setOnClickListener {
            navigateToEmail(view)
        }

        binding.buttonGoogle.setOnClickListener { _ ->
            signInWithGoogle()
        }

        binding.loginButton.setOnClickListener {
            signInWithFacebook()
        }

        binding.buttonFacebook.setOnClickListener {
            binding.loginButton.performClick()
        }

        termsSpan()

    }

    override fun onStarted() {
        println("Started-Greet")
    }

    override fun onSuccess() {
        signIn()
        Toast.makeText(context, "Success-Greet", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun signInWithGoogle() {
        viewModel.signOutFromGoogle()
        val signInIntent = viewModel.googleSignInIntent
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    viewModel.authWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w(viewModel.loginTag, "Google sign in failed", e)
                }
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun navigateToFacebook() {
        viewModel.signOutFromFacebook()
        binding.loginButton.setPermissions("email", "public_profile")
        binding.loginButton.registerCallback(
            viewModel.callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    viewModel.authWithFacebook(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(viewModel.loginTag, "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d(viewModel.loginTag, "facebook:onError", error)
                }
            })
    }

    private fun signInWithFacebook() {
        navigateToFacebook()
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

        spannableString.setSpan(clickableSpan, 32, 52, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickableSpan2, 57, 71, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

        binding.termsAndConditionsText.text = spannableString
        binding.termsAndConditionsText.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun navigateToEmail(view: View) {
        val action = GreetFragmentDirections.actionGreetFragmentToSignUpFragment()
        Navigation.findNavController(view).navigate(action)
    }

    private fun signIn() {
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}