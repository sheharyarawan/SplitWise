package com.example.splitwise.fragments.expenses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAddExpenseBinding

class AddExpenseFragment : Fragment(R.layout.fragment_add_expense) {

    lateinit var binding: FragmentAddExpenseBinding
    lateinit var viewPager: ViewPager2
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentAddExpenseBinding.bind(view)
        viewPager = requireParentFragment()
            .requireView()
            .findViewById(R.id.addExpenseVp)

        handleViewPagerClick()
        binding.IgToolbar.setNavigationOnClickListener {
            (parentFragment as AddExpenseIGSheet).dismiss()
        }


    }

    fun handleViewPagerClick(){
        binding.expensePaidBy.setOnClickListener {

            viewPager.currentItem = 1
        }

        binding.expenseSplit.setOnClickListener {
            viewPager.setCurrentItem(3,false)
        }

        binding.paidByAndSplitFor2.setOnClickListener {
            viewPager.setCurrentItem(4,false)
        }
    }

}