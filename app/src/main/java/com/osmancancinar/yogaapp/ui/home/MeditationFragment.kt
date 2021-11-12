package com.osmancancinar.yogaapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.osmancancinar.yogaapp.databinding.FragmentMeditationBinding
import com.osmancancinar.yogaapp.vm.home.MeditationVM

class MeditationFragment : Fragment() {

    private lateinit var binding: FragmentMeditationBinding
    private lateinit var viewModel: MeditationVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMeditationBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MeditationVM::class.java)
    }
}