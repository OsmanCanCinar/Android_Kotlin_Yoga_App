package com.osmancancinar.yogaapp.ui.view.home.blog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.osmancancinar.yogaapp.databinding.FragmentBlogDetailBinding
import com.osmancancinar.yogaapp.ui.viewModel.home.blog.BlogDetailVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlogDetailFragment : Fragment() {

    private lateinit var binding: FragmentBlogDetailBinding
    private val args: BlogDetailFragmentArgs by navArgs()
    private val viewModel: BlogDetailVM by viewModels()
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = args.blogId
        viewModel.getSelectedPost(id!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBlogDetailBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.selectedPost.observe(viewLifecycleOwner, Observer { blog ->
            blog?.let {
                binding.selectedPost = it
            }
        })
    }
}