package com.osmancancinar.yogaapp.ui.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.osmancancinar.yogaapp.databinding.FragmentSettingsBinding
import com.osmancancinar.yogaapp.ui.view.auth.AuthActivity
import com.osmancancinar.yogaapp.ui.viewModel.home.SettingsVM

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsVM::class.java)

        binding.signOutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            goToAuthScreen()
        }
    }

    private fun goToAuthScreen() {
        val intent = Intent(context, AuthActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}