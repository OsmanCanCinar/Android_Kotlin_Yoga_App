package com.osmancancinar.yogaapp.view.ui.home.yoga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentVinyasaYogaBinding

class VinyasaYogaFragment : Fragment() {

    private lateinit var binding: FragmentVinyasaYogaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVinyasaYogaBinding.inflate(LayoutInflater.from(context), container, false)
        return inflater.inflate(R.layout.fragment_vinyasa_yoga, container, false)
    }

    companion object {
        fun newInstance() = VinyasaYogaFragment()
    }
}