package com.example.splitwise.fragments.expenses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.splitwise.R
import com.example.splitwise.adapters.AddExpenseViewPager
import com.example.splitwise.adapters.SplitViewPager
import com.example.splitwise.databinding.FragmentSplitBinding
import com.google.android.material.tabs.TabLayoutMediator


class SplitFragment : Fragment(R.layout.fragment_split) {

    lateinit var binding: FragmentSplitBinding

    override fun onResume() {
        super.onResume()

        binding.splitViewPager.setCurrentItem(0, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentSplitBinding.bind(view)
        setUpViewPager()

        binding.splitToolbar.setNavigationOnClickListener {
            goBackToAddExpense()
        }

    }

    private fun setUpViewPager() {

        binding.splitViewPager.adapter = SplitViewPager(this)

        TabLayoutMediator(
            binding.splitTabsLayout,
            binding.splitViewPager
        ) { tab, position ->

            when (position) {
                0 -> tab.text = "Equally"
                1 -> tab.text = "Unequally"
                2 -> tab.text = "By percentage"
            }
        }.attach()
    }
    private fun goBackToAddExpense() {
        val sheet = parentFragment as? AddExpenseIGSheet
        sheet?.goToPage(0)
    }

}