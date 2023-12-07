package com.example.dchannels.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dchannels.ui.ChannelsFragment
import com.example.dchannels.ui.MyChannelsFragment


class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2 // The number of tabs

    override fun createFragment(position: Int):  Fragment {
        // Return a new fragment instance for each tab
        return when (position) {
            0 -> ChannelsFragment()
            1 -> MyChannelsFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
