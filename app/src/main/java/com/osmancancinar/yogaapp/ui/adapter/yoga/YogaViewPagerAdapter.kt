package com.osmancancinar.yogaapp.ui.adapter.yoga

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.osmancancinar.yogaapp.ui.view.home.yoga.*

class YogaViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> ChairYogaFragment.newInstance()
            1 -> HathaYogaFragment.newInstance()
            else -> HathaYogaFragment.newInstance()
        }
    }
}