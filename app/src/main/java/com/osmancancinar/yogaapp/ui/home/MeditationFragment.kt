package com.osmancancinar.yogaapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.*
import com.osmancancinar.yogaapp.utils.adapters.MeditationAdapter
import com.osmancancinar.yogaapp.databinding.FragmentMeditationBinding
import com.osmancancinar.yogaapp.models.Meditation
import com.osmancancinar.yogaapp.viewModels.home.MeditationVM

class MeditationFragment : Fragment() {

    private lateinit var binding: FragmentMeditationBinding
    private lateinit var viewModel: MeditationVM
    private lateinit var database: FirebaseFirestore
    //private lateinit var meditationList: ArrayList<Meditation>
    private val meditationList = arrayListOf<Meditation>()
    private lateinit var mAdapter: MeditationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseFirestore.getInstance()
        //meditationList = arrayListOf()
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

        viewModel = ViewModelProviders.of(this).get(MeditationVM::class.java)

        //downloadFromFB()

        mAdapter = MeditationAdapter(meditationList)

        binding.meditationList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }

        //mAdapter.differ.submitList(meditationList)

        downloadFromFB()
    }

    private fun downloadFromFB() {
        database.collection("meditation")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Meditation Error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            meditationList.add(dc.document.toObject(Meditation::class.java))
                        }
                    }
                    mAdapter.notifyDataSetChanged()
                }

            })
    }
}