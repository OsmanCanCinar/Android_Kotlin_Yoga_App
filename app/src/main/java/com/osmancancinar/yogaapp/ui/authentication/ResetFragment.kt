package com.osmancancinar.yogaapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentResetBinding
import com.osmancancinar.yogaapp.utils.adapters.AuthListener
import com.osmancancinar.yogaapp.viewModels.auth.ResetVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetFragment : Fragment(), AuthListener {

    private lateinit var binding: FragmentResetBinding
    private val viewModel: ResetVM by viewModels()
    private var emailFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.authListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailClickHandler()

        binding.resetButton.setOnClickListener {
            checkAndReset(view)
        }
    }

    private fun checkAndReset(view: View) {
        if (binding.emailTextReset.text.isNullOrEmpty()) {
            Toast.makeText(context, getString(R.string.error_msg_empty), Toast.LENGTH_SHORT).show()
        } else if (emailFlag) {
            Toast.makeText(context, getString(R.string.error_msg), Toast.LENGTH_SHORT).show()
        } else {
            resetPassword(view)
        }
    }

    private fun resetPassword(view: View) {
        val email = binding.emailTextReset.text.toString()
        viewModel.resetPassword(email)
    }

    override fun onSuccess() {
        goBackToGreet()
        Toast.makeText(context, "Please Check your Email", Toast.LENGTH_SHORT).show()
    }

    private fun goBackToGreet() {
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun emailClickHandler() {
        binding.emailTextReset.doOnTextChanged { text, _, _, _ ->
            val txt = text.toString()
            binding.emailTextInputLayoutReset.apply {
                helperText = viewModel.validateEmail(txt)
                error = viewModel.validateEmail(txt)
                emailFlag = false
                if (!error.isNullOrEmpty()) {
                    emailFlag = true
                }
            }
        }

        binding.emailTextReset.setOnFocusChangeListener { _, focused ->
            if (!focused && binding.emailTextReset.text.isNullOrEmpty()) {
                binding.emailTextInputLayoutReset.apply {
                    helperText = getString(R.string.required)
                    error = null
                    emailFlag = false
                }
            }
        }
    }

    override fun onStarted() {
        println("Started-SignUp")
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
            emailTextInputLayoutReset.apply {
                helperText = getString(R.string.required)
                error = null
                emailFlag = false
            }
        }
    }

}