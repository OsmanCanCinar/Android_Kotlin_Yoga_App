package com.osmancancinar.yogaapp.view.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentSignInBinding
import com.osmancancinar.yogaapp.view.ui.home.HomeActivity
import com.osmancancinar.yogaapp.view.listeners.AuthListener
import com.osmancancinar.yogaapp.viewModel.auth.SignInVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(), AuthListener {

    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInVM by viewModels()
    private var emailFlag: Boolean = false
    private var passwordFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.authListener = this
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

        clickHandler()

        binding.buttonSignIn.setOnClickListener {
            checkAndSignIn()
        }

        binding.goToSignUpText.setOnClickListener {
            navigateToSignUp(view)
        }

        binding.backToOptionsButtonSignIn.setOnClickListener {
            goBackToOptions(view)
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
        } else if (emailFlag || passwordFlag) {
            Toast.makeText(context, getString(R.string.error_msg), Toast.LENGTH_SHORT).show()
        } else {
            logIn()
        }
    }

    private fun logIn() {
        val email = binding.emailTextSignIn.text.toString()
        val password = binding.passwordTextSignIn.text.toString()
        viewModel.signInWithEmail(email, password)
    }

    private fun emailClickHandler() {
        binding.emailTextSignIn.doOnTextChanged { text, _, _, _ ->
            val txt = text.toString()
            binding.emailTextInputLayoutSignIn.apply {
                helperText = viewModel.validateEmail(txt)
                error = viewModel.validateEmail(txt)
                emailFlag = false
                if (!error.isNullOrEmpty()) {
                    emailFlag = true
                }
            }
        }

        binding.emailTextSignIn.setOnFocusChangeListener { _, focused ->
            if (!focused && binding.emailTextSignIn.text.isNullOrEmpty()) {
                binding.emailTextInputLayoutSignIn.apply {
                    helperText = null
                    error = null
                    emailFlag = false
                }
            }
        }
    }

    private fun passwordClickHandler() {
        binding.passwordTextSignIn.doOnTextChanged { text, _, _, _ ->
            val txt = text.toString()
            binding.passwordTextInputLayoutSignIn.apply {
                helperText = viewModel.validatePassword(txt)
                error = viewModel.validatePassword(txt)
                passwordFlag = false
                if (!error.isNullOrEmpty()) {
                    passwordFlag = true
                }
            }
        }

        binding.passwordTextSignIn.setOnFocusChangeListener { _, focused ->
            if (!focused && binding.passwordTextSignIn.text.isNullOrEmpty()) {
                binding.passwordTextInputLayoutSignIn.apply {
                    helperText = null
                    error = null
                    passwordFlag = false
                }
            }
        }
    }

    private fun clickHandler() {
        emailClickHandler()
        passwordClickHandler()
    }

    private fun navigateToSignUp(view: View) {
        val actionToSignUp = SignInFragmentDirections.actionSignInFragmentToSignUpFragment3()
        Navigation.findNavController(view).navigate(actionToSignUp)
    }

    private fun goBackToOptions(view: View) {
        val actionToOptions = SignInFragmentDirections.actionGlobalGreetFragment()
        Navigation.findNavController(view).navigate(actionToOptions)
    }

    private fun resetPassword(view: View) {
        val actionToResetPassword = SignInFragmentDirections.actionSignInFragmentToResetFragment()
        Navigation.findNavController(view).navigate(actionToResetPassword)
    }

    private fun signIn() {
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onStarted() {
        println("Started-SignUp")
    }

    override fun onSuccess() {
        signIn()
        Toast.makeText(context, "Success-Greet", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        setInitialSetUp()
    }

    private fun setInitialSetUp() {
        binding.apply {

            emailTextInputLayoutSignIn.apply {
                helperText = null
                error = null
                emailFlag = false
            }

            passwordTextInputLayoutSignIn.apply {
                helperText = null
                error = null
                passwordFlag = false
            }
        }
    }
}