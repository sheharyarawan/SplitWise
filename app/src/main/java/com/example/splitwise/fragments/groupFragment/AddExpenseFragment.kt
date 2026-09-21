package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAddExpenseBinding


class AddExpenseFragment : Fragment(R.layout.fragment_add_expense) {

    lateinit var binding: FragmentAddExpenseBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentAddExpenseBinding.bind(view)
        handleViewPagerClick()
    }

    fun handleViewPagerClick(){
        binding.expensePaidBy.setOnClickListener {

            val viewPager = requireParentFragment()
                .requireView()
                .findViewById<ViewPager2>(R.id.addExpenseVp)

            viewPager.currentItem = 1
        }
    }

}