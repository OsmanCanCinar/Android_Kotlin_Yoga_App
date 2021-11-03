package com.osmancancinar.yogaapp.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentSignInBinding
import com.osmancancinar.yogaapp.viewModels.auth.SignInVM

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignInBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignInVM::class.java)

        binding.goToSignUpText.setOnClickListener {
            viewModel.navigateToSignUp(view)
        }

        binding.backToOptionsButtonSignIn.setOnClickListener {
            viewModel.goBackToOptions(view)
        }

        binding.buttonSignIn.setOnClickListener {
            checkAndSignIn()
        }

        binding.forgetPasswordText.setOnClickListener {
            viewModel.resetPassword(view)
        }
    }

    private fun checkAndSignIn() {
        if (binding.emailTextSignIn.text.isNullOrEmpty() ||
            binding.passwordTextSignIn.text.isNullOrEmpty()
        ) {
            Toast.makeText(context, getString(R.string.error_msg_empty), Toast.LENGTH_SHORT).show()
        } else {
            viewModel.signIn(requireActivity())
        }
    }
}