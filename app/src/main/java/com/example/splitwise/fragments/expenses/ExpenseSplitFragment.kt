package com.example.splitwise.fragments.expenses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentExpenseSplitBinding

class ExpenseSplitFragment : Fragment(R.layout.fragment_expense_split) {

    lateinit var binding: FragmentExpenseSplitBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentExpenseSplitBinding.bind(view)
        handleClicks()

    }

    fun handleClicks(){
        binding.expenseSplitBw2MoreOptions.setOnClickListener {
            findNavController().navigate(R.id.action_expenseSplitFragment_to_splitFragment)
        }

        binding.expenseSplitBw2Toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}