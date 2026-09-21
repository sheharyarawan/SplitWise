package com.example.splitwise.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.splitwise.fragments.groupFragment.AddExpenseFragment
import com.example.splitwise.fragments.groupFragment.PaidAmountFragment
import com.example.splitwise.fragments.groupFragment.PaidByFragment

class AddExpenseViewPager(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
       return when(position){
           0-> AddExpenseFragment()
           1-> PaidByFragment()
           2-> PaidAmountFragment()
           else -> throw IllegalArgumentException("")
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

}