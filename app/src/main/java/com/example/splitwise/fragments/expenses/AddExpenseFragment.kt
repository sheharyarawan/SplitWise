package com.example.splitwise.fragments.expenses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAddExpenseBinding

class AddExpenseFragment : Fragment(R.layout.fragment_add_expense) {

    lateinit var binding: FragmentAddExpenseBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentAddExpenseBinding.bind(view)

        handleClicks()

    }

    fun handleClicks(){
        binding.IgToolbar.setNavigationOnClickListener {
            val bottomSheet =
                requireParentFragment().requireParentFragment() as AddExpenseIGSheet

            bottomSheet.dismiss()
        }

        binding.paidByAndSplitFor2.setOnClickListener {
            findNavController().navigate(R.id.action_addExpenseFragment_to_expenseSplitFragment)
        }

        binding.expensePaidBy.setOnClickListener {
            findNavController().navigate(R.id.action_addExpenseFragment_to_paidByFragment2)
        }
        binding.expenseSplit.setOnClickListener {
            findNavController().navigate(R.id.action_addExpenseFragment_to_splitFragment)
        }
    }
}