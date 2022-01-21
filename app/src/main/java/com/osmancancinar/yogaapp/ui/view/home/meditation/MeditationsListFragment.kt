package com.osmancancinar.yogaapp.ui.view.home.meditation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.osmancancinar.yogaapp.data.model.MeditationCategoriesList
import com.osmancancinar.yogaapp.ui.adapter.meditation.MeditationsListAdapter
import com.osmancancinar.yogaapp.databinding.FragmentMeditationBinding
import com.osmancancinar.yogaapp.ui.adapter.meditation.MeditationCategoriesListListener
import com.osmancancinar.yogaapp.ui.view.listeners.FirebaseListener
import com.osmancancinar.yogaapp.ui.viewModel.home.meditation.MeditationCategoriesListVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeditationsListFragment : Fragment(), FirebaseListener {

    private lateinit var binding: FragmentMeditationBinding
    private val viewModel: MeditationCategoriesListVM by viewModels()
    private lateinit var mAdapter: MeditationsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.firebaseListener = this
    }

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

        navigateToMeditations(view)

        binding.meditationCategoryList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }
    }

    private fun feedTheAdapter() {
        viewModel.getMeditationCategoriesList()
            .observe(this, Observer<List<MeditationCategoriesList>> { categories ->
                mAdapter.submitList(categories)
            })
    }

    override fun onSuccess() {
        feedTheAdapter()
    }

    override fun onResume() {
        super.onResume()
        feedTheAdapter()
    }

    private fun navigateToMeditations(view: View) {
        mAdapter = MeditationsListAdapter(MeditationCategoriesListListener {
            val action =
                MeditationsListFragmentDirections.actionMeditationFragmentToMeditationCategoryFragment(
                    it
                )
            Navigation.findNavController(view).navigate(action)
        })
    }

    override fun onStarted() {
        println("Started-Display")
    }

    override fun onFailure(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}