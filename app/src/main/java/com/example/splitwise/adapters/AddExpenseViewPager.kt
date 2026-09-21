package com.example.splitwise.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.splitwise.fragments.expenses.AddExpenseFragment
import com.example.splitwise.fragments.expenses.PaidAmountFragment
import com.example.splitwise.fragments.expenses.PaidByFragment
import com.example.splitwise.fragments.expenses.SplitFragment

class AddExpenseViewPager(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
       return when(position){
           0-> AddExpenseFragment()
           1-> PaidByFragment()
           2-> PaidAmountFragment()
           3-> SplitFragment()
           else -> throw IllegalArgumentException("")
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

}