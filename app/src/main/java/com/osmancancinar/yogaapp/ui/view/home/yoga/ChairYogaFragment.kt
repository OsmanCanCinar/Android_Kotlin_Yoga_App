package com.osmancancinar.yogaapp.ui.view.home.yoga

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.osmancancinar.yogaapp.data.model.Yoga
import com.osmancancinar.yogaapp.databinding.FragmentChairYogaBinding
import com.osmancancinar.yogaapp.ui.adapter.yoga.YogaAdapter
import com.osmancancinar.yogaapp.ui.adapter.yoga.YogaListener
import com.osmancancinar.yogaapp.ui.view.listeners.FirebaseListener
import com.osmancancinar.yogaapp.ui.viewModel.home.yoga.YogaVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChairYogaFragment : Fragment(), FirebaseListener {

    companion object {
        fun newInstance() = ChairYogaFragment()
    }

    private lateinit var binding: FragmentChairYogaBinding
    private lateinit var mAdapter: YogaAdapter
    private val viewModel: YogaVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.firebaseListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChairYogaBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToYogaVideo(view)

        binding.chairYogaList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.displayYogaList("chair")
    }

    private fun feedTheAdapter() {
        viewModel.getYogaList()
            .observe(viewLifecycleOwner, Observer<List<Yoga>> { yoga ->
                mAdapter.submitList(yoga)
            })
    }

    private fun navigateToYogaVideo(view: View) {
        mAdapter = YogaAdapter(YogaListener {

            val intent = Intent(context, TemplateActivity::class.java)
            intent.putExtra("id", it)
            requireActivity().startActivity(intent)
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