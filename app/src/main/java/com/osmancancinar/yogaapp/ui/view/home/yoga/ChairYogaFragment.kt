package com.osmancancinar.yogaapp.ui.view.home.yoga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.osmancancinar.yogaapp.databinding.FragmentChairYogaBinding

class ChairYogaFragment : Fragment() {

    private lateinit var binding: FragmentChairYogaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChairYogaBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = ChairYogaFragment()
    }
}