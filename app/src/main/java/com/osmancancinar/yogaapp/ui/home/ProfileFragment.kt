package com.osmancancinar.yogaapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentProfileBinding
import com.osmancancinar.yogaapp.vm.home.ProfileVM

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileVM
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ProfileVM::class.java)
        setFields()
    }

    private fun setFields() {
        val currentUser = auth.currentUser
        binding.emailTxt.text = currentUser?.email.toString()
        binding.nameTxt.text = currentUser?.displayName.toString()

        val imgUrl = currentUser?.photoUrl
        if (imgUrl == null) {
            binding.profileImage.setImageResource(R.drawable.ic_launcher_background)
        } else {
            Glide.with(requireActivity()).load(currentUser?.photoUrl).into(binding.profileImage)
        }
    }
}