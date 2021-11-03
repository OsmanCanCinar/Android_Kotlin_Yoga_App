package com.osmancancinar.yogaapp.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentSignUpBinding
import com.osmancancinar.yogaapp.viewModels.auth.SignUpVM

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignUpBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SignUpVM::class.java)

        binding.goToSignInText.setOnClickListener {
            viewModel.navigateToSignIn(it)
        }

        clickHandler()

        binding.buttonSignUp.setOnClickListener {
            checkAndSignUp()
        }

        binding.backToOptionsButton.setOnClickListener {
            viewModel.goBackToOptions(view)
        }

        binding.termsAndConditionsText.setOnClickListener {
            viewModel.showDialog(requireContext())
        }
    }

    private fun checkAndSignUp() {
        if (binding.nameText.text.isNullOrEmpty() ||
            binding.emailText.text.isNullOrEmpty() ||
            binding.passwordText.text.isNullOrEmpty()
        ) {
            Toast.makeText(context, getString(R.string.error_msg_empty), Toast.LENGTH_SHORT).show()
        } else {
            viewModel.signUp(requireActivity())
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
            }

            emailTextInputLayout.apply {
                helperText = getString(R.string.required)
                error = null
            }

            passwordTextInputLayout.apply {
                helperText = getString(R.string.required)
                error = null
            }
        }
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
            }
        }

        binding.emailText.setOnFocusChangeListener { _, focused ->
            if (!focused && binding.emailText.text.isNullOrEmpty()) {
                binding.emailTextInputLayout.apply {
                    helperText = getString(R.string.required)
                    error = null
                }
            }
        }
    }

    private fun nameClickHandler() {
        binding.nameText.doOnTextChanged { text, _, _, _ ->
            val txt = text.toString()
            binding.nameTextInputLayout.apply {
                helperText = viewModel.validateName(txt)
                error = viewModel.validateName(txt)
            }
        }

        binding.nameText.setOnFocusChangeListener { _, focused ->
            if (!focused && binding.nameText.text.isNullOrEmpty()) {
                binding.nameTextInputLayout.apply {
                    helperText = getString(R.string.required)
                    error = null
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
            }
        }

        binding.passwordText.setOnFocusChangeListener { _, focused ->
            if (!focused && binding.passwordText.text.isNullOrEmpty()) {
                binding.passwordTextInputLayout.apply {
                    helperText = getString(R.string.required)
                    error = null
                }
            }
        }
    }
}