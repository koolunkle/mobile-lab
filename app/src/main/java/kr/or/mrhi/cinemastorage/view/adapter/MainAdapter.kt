package kr.or.mrhi.cinemastorage.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.or.mrhi.cinemastorage.view.fragment.ListFragment
import kr.or.mrhi.cinemastorage.view.fragment.ProfileFragment
import kr.or.mrhi.cinemastorage.view.fragment.ReviewFragment

class MainAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragmentList = arrayListOf(ListFragment(), ReviewFragment(), ProfileFragment())

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemCount() = fragmentList.size
}