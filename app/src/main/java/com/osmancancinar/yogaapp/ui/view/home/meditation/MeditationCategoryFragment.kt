package com.osmancancinar.yogaapp.ui.view.home.meditation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.osmancancinar.yogaapp.data.model.MeditationCategory
import com.osmancancinar.yogaapp.databinding.FragmentMeditationCategoryBinding
import com.osmancancinar.yogaapp.ui.view.listeners.FirebaseListener
import com.osmancancinar.yogaapp.ui.viewModel.home.meditation.MeditationCategoryVM
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.osmancancinar.yogaapp.ui.adapter.meditation.MeditationCategoryListener
import com.osmancancinar.yogaapp.ui.adapter.meditation.MeditationCategoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeditationCategoryFragment : Fragment(), FirebaseListener {

    private lateinit var binding: FragmentMeditationCategoryBinding
    private val viewModel: MeditationCategoryVM by viewModels()
    private lateinit var mAdapter: MeditationCategoryAdapter
    private val args: MeditationCategoryFragmentArgs by navArgs()
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.firebaseListener = this
        id = args.categoryId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMeditationCategoryBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToMeditation(view)

        binding.meditationCategory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        viewModel.displayMeditationCategory(id!!)
    }

    private fun navigateToMeditation(view: View) {
        mAdapter = MeditationCategoryAdapter(MeditationCategoryListener {
            val action =
                MeditationCategoryFragmentDirections.actionMeditationCategoryFragmentToMeditationDetailFragment(
                    it
                )
            Navigation.findNavController(view).navigate(action)
        })
    }

    private fun feedTheAdapter() {
        viewModel.getMeditationCategory()
            .observe(viewLifecycleOwner, Observer<List<MeditationCategory>> { categories ->
                mAdapter.submitList(categories)
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