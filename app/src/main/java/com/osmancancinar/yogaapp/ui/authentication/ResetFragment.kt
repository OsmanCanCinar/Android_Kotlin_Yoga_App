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
import com.osmancancinar.yogaapp.databinding.FragmentResetBinding
import com.osmancancinar.yogaapp.viewModels.auth.ResetVM

class ResetFragment : Fragment() {

    private lateinit var binding: FragmentResetBinding
    private lateinit var viewModel: ResetVM
    private var emailFlag: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetBinding.inflate(LayoutInflater.from(context),container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ResetVM::class.java)

        emailClickHandler()

        binding.resetButton.setOnClickListener {
           checkAndReset(view)
        }
    }

    private fun checkAndReset(view: View) {
        if (binding.emailTextReset.text.isNullOrEmpty()) {
            Toast.makeText(context, getString(R.string.error_msg_empty), Toast.LENGTH_SHORT).show()
        } else if(emailFlag) {
            Toast.makeText(context, getString(R.string.error_msg), Toast.LENGTH_SHORT).show()
        } else{
            viewModel.resetPassword(view)
        }
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