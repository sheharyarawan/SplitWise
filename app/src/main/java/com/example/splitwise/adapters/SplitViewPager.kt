package com.example.splitwise.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.splitwise.fragments.expenses.EquallyFragment
import com.example.splitwise.fragments.expenses.PercentageFragment
import com.example.splitwise.fragments.expenses.UnequallyFragment

class SplitViewPager(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> EquallyFragment()
            1-> UnequallyFragment()
            2-> PercentageFragment()
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}