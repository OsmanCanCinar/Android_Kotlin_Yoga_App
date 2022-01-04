package com.osmancancinar.yogaapp.view.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.osmancancinar.yogaapp.adapter.MeditationCategoryListener
import com.osmancancinar.yogaapp.adapter.MeditationsListAdapter
import com.osmancancinar.yogaapp.data.model.MeditationCategory
import com.osmancancinar.yogaapp.databinding.FragmentMeditationBinding
import com.osmancancinar.yogaapp.utils.CustomSharedPreferences
import com.osmancancinar.yogaapp.view.listeners.AuthListener
import com.osmancancinar.yogaapp.viewModel.home.MeditationVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeditationsListFragment : Fragment(), AuthListener {

    private lateinit var binding: FragmentMeditationBinding
    private val viewModel: MeditationVM by viewModels()
    private lateinit var meditationCategory: MutableList<MeditationCategory>
    private lateinit var mAdapter: MeditationsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        meditationCategory = arrayListOf()
        viewModel.authListener = this
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

        binding.meditationList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }
        populate()
    }

    private fun populate() {
        viewModel.displayMeditationCategoriesList()
    }

    private fun navigateToMeditations(view: View) {
        //navigate to detail here
        mAdapter = MeditationsListAdapter(MeditationCategoryListener {
            Toast.makeText(context, "${it}", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onStarted() {
        println("Started-Display")
    }


    override fun onSuccess() {
        mAdapter.submitList(viewModel.list)
    }

    override fun onFailure(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}