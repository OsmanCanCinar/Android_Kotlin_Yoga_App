package com.osmancancinar.yogaapp.ui.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentSignInBinding
import com.osmancancinar.yogaapp.ui.home.HomeActivity
import com.osmancancinar.yogaapp.viewModels.auth.SignInVM

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInVM
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignInVM::class.java)

        binding.goToSignUpText.setOnClickListener {
            navigateToSignUp(view)
        }

        binding.backToOptionsButtonSignIn.setOnClickListener {
            goBackToOptions(view)
        }

        binding.buttonSignIn.setOnClickListener {
            checkAndSignIn()
        }

        binding.forgetPasswordText.setOnClickListener {
            resetPassword(view)
        }
    }

    private fun checkAndSignIn() {
        if (binding.emailTextSignIn.text.isNullOrEmpty() ||
            binding.passwordTextSignIn.text.isNullOrEmpty()
        ) {
            Toast.makeText(context, getString(R.string.error_msg_empty), Toast.LENGTH_SHORT).show()
        } else {
            val email = binding.emailTextSignIn.text.toString()
            val password = binding.passwordTextSignIn.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                signIn(requireActivity())
            }.addOnFailureListener {
                //Change this later
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToSignUp(view: View) {
        val actionToSignUp = SignInFragmentDirections.actionSignInFragmentToSignUpFragment3()
        Navigation.findNavController(view).navigate(actionToSignUp)
    }

    private fun goBackToOptions(view: View) {
        val actionToOptions = SignInFragmentDirections.actionGlobalGreetFragment()
        Navigation.findNavController(view).navigate(actionToOptions)
    }

    private fun signIn(activity: Activity) {
        val intent = Intent(activity, HomeActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    private fun resetPassword(view: View) {
        val actionToResetPassword = SignInFragmentDirections.actionSignInFragmentToResetFragment()
        Navigation.findNavController(view).navigate(actionToResetPassword)
    }
}