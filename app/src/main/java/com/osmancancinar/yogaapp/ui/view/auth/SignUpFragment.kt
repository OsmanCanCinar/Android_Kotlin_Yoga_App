package com.osmancancinar.yogaapp.ui.view.auth

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
import com.osmancancinar.yogaapp.databinding.FragmentSignUpBinding
import com.osmancancinar.yogaapp.ui.view.home.HomeActivity
import com.osmancancinar.yogaapp.ui.view.listeners.FirebaseListener
import com.osmancancinar.yogaapp.ui.viewModel.auth.SignUpVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(), FirebaseListener {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpVM by viewModels()

    private var nameFlag: Boolean = false
    private var emailFlag: Boolean = false
    private var passwordFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.firebaseListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
         */

        clickHandler()

        binding.buttonSignUp.setOnClickListener {
            checkAndSignUp()
        }

        binding.backToOptionsButton.setOnClickListener {
            goBackToOptions(view)
        }

        binding.goToSignInText.setOnClickListener {
            navigateToSignIn(it)
        }
    }

    private fun checkAndSignUp() {
        if (binding.nameText.text.isNullOrEmpty() ||
            binding.emailText.text.isNullOrEmpty() ||
            binding.passwordText.text.isNullOrEmpty()
        ) {
            Toast.makeText(context, getString(R.string.error_msg_empty), Toast.LENGTH_SHORT).show()
        } else if (nameFlag || emailFlag || passwordFlag) {
            Toast.makeText(context, getString(R.string.error_msg), Toast.LENGTH_SHORT).show()
        } else {
            signUp()
        }
    }

    private fun signUp() {
        val email = binding.emailText.text.toString()
        val username = binding.nameText.text.toString()
        val password = binding.passwordText.text.toString()

        viewModel.signUpWithEmail(email, username, password)
    }

    override fun onStarted() {
        //progressbar.visibility = View.VISIBLE
        println("Started-SignUp")
    }

    override fun onSuccess() {
        //progressbar.visibility = View.GONE
        signIn()
        Toast.makeText(context, "Success-Greet", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(msg: String) {
        //progressbar.visibility = View.GONE
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun clickHandler() {
        nameClickHandler()
        emailClickHandler()
        passwordClickHandler()
    }

    private fun emailClickHandler() {
        binding.emailText.doOnTextChanged { text, _, _, _ ->
            val txt = text.toString()
            binding.emailTextInputLayout.apply {
                helperText = viewModel.validateEmail(txt)
                error = viewModel.validateEmail(txt)
                emailFlag = false
                if (!error.isNullOrEmpty()) {
                    emailFlag = true
                }
            }
        }

        binding.emailText.setOnFocusChangeListener { _, focused ->
            if (!focused && binding.emailText.text.isNullOrEmpty()) {
                binding.emailTextInputLayout.apply {
                    helperText = getString(R.string.required)
                    error = null
                    emailFlag = false
                }
            }
        }
    }

    private fun nameClickHandler() {
        binding.nameText.doOnTextChanged { text, _, _, _ ->
            val txt = text.toString()
            binding.nameTextInputLayout.apply {
                helperText = viewModel.validateUsername(txt)
                error = viewModel.validateUsername(txt)
                nameFlag = false
                if (!error.isNullOrEmpty()) {
                    nameFlag = true
                }
            }
        }

        binding.nameText.setOnFocusChangeListener { _, focused ->
            if (!focused && binding.nameText.text.isNullOrEmpty()) {
                binding.nameTextInputLayout.apply {
                    helperText = getString(R.string.required)
                    error = null
                    nameFlag = false
                }
            }
        }
    }

    private fun passwordClickHandler() {
        binding.passwordText.doOnTextChanged { text, _, _, _ ->
            val txt = text.toString()
            binding.passwordTextInputLayout.apply {
                helperText = viewModel.validatePassword(txt)
                error = viewModel.validatePassword(txt)
                passwordFlag = false
                if (!error.isNullOrEmpty()) {
                    passwordFlag = true
                }
            }
        }

        binding.passwordText.setOnFocusChangeListener { _, focused ->
            if (!focused && binding.passwordText.text.isNullOrEmpty()) {
                binding.passwordTextInputLayout.apply {
                    helperText = getString(R.string.required)
                    error = null
                    passwordFlag = false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setInitialSetUp()
    }

    private fun setInitialSetUp() {
        binding.apply {

            nameTextInputLayout.apply {
                helperText = getString(R.string.required)
                error = null
                nameFlag = false
            }

            emailTextInputLayout.apply {
                helperText = getString(R.string.required)
                error = null
                emailFlag = false
            }

            passwordTextInputLayout.apply {
                helperText = getString(R.string.required)
                error = null
                passwordFlag = false
            }
        }
    }

    private fun navigateToSignIn(view: View) {
        val actionToSignIn = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment3()
        Navigation.findNavController(view).navigate(actionToSignIn)
    }

    private fun goBackToOptions(view: View) {
        val actionToOptions = SignUpFragmentDirections.actionGlobalGreetFragment()
        Navigation.findNavController(view).navigate(actionToOptions)
    }

    private fun signIn() {
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}