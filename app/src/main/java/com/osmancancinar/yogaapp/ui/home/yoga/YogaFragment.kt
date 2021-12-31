package com.osmancancinar.yogaapp.ui.home.yoga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.osmancancinar.yogaapp.utils.adapters.MeditationViewPagerAdapter
import com.osmancancinar.yogaapp.databinding.FragmentYogaBinding
import com.osmancancinar.yogaapp.viewModels.home.YogaVM

class YogaFragment : Fragment() {

    private lateinit var binding: FragmentYogaBinding
    private lateinit var viewModel: YogaVM
    private val tags2 = arrayOf("CHAIR", "HATHA", "PRENATAL", "VINYASA")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYogaBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(YogaVM::class.java)

        binding.viewPager2.adapter = MeditationViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayoutYoga, binding.viewPager2) { tab, position ->
            tab.text = tags2[position]
        }.attach()


        binding.tabLayoutYoga.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager2.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayoutYoga.selectTab(binding.tabLayoutYoga.getTabAt(position))
            }
        }
        )
    }
}
