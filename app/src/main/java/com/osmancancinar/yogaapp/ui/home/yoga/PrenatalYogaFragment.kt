package com.osmancancinar.yogaapp.ui.home.yoga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.osmancancinar.yogaapp.databinding.FragmentPregnantYogaBinding

class PrenatalYogaFragment : Fragment() {

    private lateinit var binding: FragmentPregnantYogaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPregnantYogaBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = PrenatalYogaFragment()
    }
}