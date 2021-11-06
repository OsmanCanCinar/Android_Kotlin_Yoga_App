package com.osmancancinar.yogaapp.ui.authentication

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentSignUpBinding
import com.osmancancinar.yogaapp.ui.home.HomeActivity
import com.osmancancinar.yogaapp.viewModels.auth.SignUpVM

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpVM
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private var nameFlag: Boolean = false
    private var emailFlag: Boolean = false
    private var passwordFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignUpBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SignUpVM::class.java)

        binding.goToSignInText.setOnClickListener {
            navigateToSignIn(it)
        }

        clickHandler()

        binding.buttonSignUp.setOnClickListener {
            checkAndSignUp()
        }

        binding.backToOptionsButton.setOnClickListener {
            goBackToOptions(view)
        }

        binding.termsAndConditionsText.setOnClickListener {
           showDialog()
        }
    }

    private fun checkAndSignUp() {
        if (binding.nameText.text.isNullOrEmpty() ||
            binding.emailText.text.isNullOrEmpty() ||
            binding.passwordText.text.isNullOrEmpty()
        ) {
            Toast.makeText(context, getString(R.string.error_msg_empty), Toast.LENGTH_SHORT).show()
        } else if(nameFlag || emailFlag || passwordFlag) {
            Toast.makeText(context, getString(R.string.error_msg), Toast.LENGTH_SHORT).show()
        } else {
            signUp(requireActivity())
        }
    }

    private fun signUp(activity: Activity) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            Toast.makeText(context,getString(R.string.successful_msg),Toast.LENGTH_LONG).show()
            addToDatabase()
            val intent = Intent(activity, HomeActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }.addOnFailureListener {
            Toast.makeText(context,it.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

    private fun addToDatabase() {
        val email = binding.emailText.text.toString()
        val username = binding.nameText.text.toString()

        val user = hashMapOf (
            "userEmail" to email,
            "userName" to username
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
                helperText = viewModel.validateName(txt)
                error = viewModel.validateName(txt)
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

    private fun navigateToSignIn(view: View) {
        val actionToSignIn = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment3()
        Navigation.findNavController(view).navigate(actionToSignIn)
    }

    private fun goBackToOptions(view: View) {
        val actionToOptions = SignUpFragmentDirections.actionGlobalGreetFragment()
        Navigation.findNavController(view).navigate(actionToOptions)
    }

    private fun showDialog() {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.terms_and_conditions)
                .setMessage(R.string.terms)
                .setPositiveButton(R.string.dismiss) { _, _ -> }
                .show()
    }
}