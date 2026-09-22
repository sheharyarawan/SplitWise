package com.example.splitwise.fragments.expenses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentPaidByBinding

class PaidByFragment : Fragment(R.layout.fragment_paid_by) {

    lateinit var binding: FragmentPaidByBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentPaidByBinding.bind(view)
        binding.multiplePeople.setOnClickListener {
            val viewPager = requireParentFragment()
                .requireView()
                .findViewById<ViewPager2>(R.id.addExpenseVp)

            viewPager.currentItem = 2
        }
        binding.whoPaidToolbar.setNavigationOnClickListener {

            (parentFragment as? AddExpenseIGSheet)?.goBack()
        }
    }
}