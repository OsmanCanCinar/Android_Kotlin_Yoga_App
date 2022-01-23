package com.osmancancinar.yogaapp.ui.view.home.blog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.osmancancinar.yogaapp.data.model.Blog
import com.osmancancinar.yogaapp.databinding.FragmentBlogBinding
import com.osmancancinar.yogaapp.ui.adapter.blog.BlogAdapter
import com.osmancancinar.yogaapp.ui.adapter.blog.BlogListener
import com.osmancancinar.yogaapp.ui.view.listeners.FirebaseListener
import com.osmancancinar.yogaapp.ui.viewModel.home.blog.BlogVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlogFragment : Fragment(), FirebaseListener {

    private lateinit var binding: FragmentBlogBinding
    private lateinit var mAdapter: BlogAdapter
    private val viewModel: BlogVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.firebaseListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBlogBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToBlogDetail(view)

        binding.blogList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    private fun navigateToBlogDetail(view: View) {
        mAdapter = BlogAdapter(BlogListener {
            val action = BlogFragmentDirections.actionBlogFragmentToBlogDetailFragment(it)
            Navigation.findNavController(view).navigate(action)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.displayBlogList()
    }

    private fun feedTheAdapter() {
        viewModel.getBlogList()
            .observe(viewLifecycleOwner, Observer<List<Blog>> { blog ->
                mAdapter.submitList(blog)
            })
    }

    override fun onSuccess() {
        feedTheAdapter()
    }

    override fun onStarted() {
        println("Started-Display")
    }

    override fun onFailure(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}