package com.osmancancinar.yogaapp.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentGreetBinding
import com.osmancancinar.yogaapp.viewModels.auth.GreetVM

class GreetFragment : Fragment() {

    private lateinit var binding: FragmentGreetBinding
    private lateinit var viewModel: GreetVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGreetBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GreetVM::class.java)

        binding.buttonEmail.setOnClickListener {
            navigateToEmail(view)
        }

        binding.buttonFacebook.setOnClickListener {
            navigateToFacebook()
        }

        binding.buttonGoogle.setOnClickListener {
            navigateToGoogle()
        }
    }

    fun navigateToEmail(view : View) {
        val action = GreetFragmentDirections.actionGreetFragmentToSignUpFragment()
        Navigation.findNavController(view).navigate(action)
    }

    fun navigateToFacebook() {
        Toast.makeText(context,getString(R.string.feature), Toast.LENGTH_SHORT).show()
    }

    fun navigateToGoogle() {
        Toast.makeText(context,getString(R.string.feature), Toast.LENGTH_SHORT).show()
    }
}