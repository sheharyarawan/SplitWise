package com.example.splitwise.fragments.expenses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentExpenseSplitBinding

class ExpenseSplitFragment : Fragment(R.layout.fragment_expense_split) {

    lateinit var binding: FragmentExpenseSplitBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentExpenseSplitBinding.bind(view)
        binding.expenseSplitBw2MoreOptions.setOnClickListener {
            val viewPager = requireParentFragment()
                .requireView()
                .findViewById<ViewPager2>(R.id.addExpenseVp)
            viewPager.setCurrentItem(3,false)
        }
    }
}