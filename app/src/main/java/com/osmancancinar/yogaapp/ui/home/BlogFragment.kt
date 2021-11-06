package com.osmancancinar.yogaapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentBlogBinding
import com.osmancancinar.yogaapp.viewModels.home.BlogVM

class BlogFragment : Fragment() {

    private lateinit var binding: FragmentBlogBinding
    private lateinit var viewModel: BlogVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBlogBinding.inflate(LayoutInflater.from(context),container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(BlogVM::class.java)
    }

}