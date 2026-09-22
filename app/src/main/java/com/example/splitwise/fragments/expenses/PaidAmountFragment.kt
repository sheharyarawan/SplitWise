package com.example.splitwise.fragments.expenses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentPaidAmountBinding

class PaidAmountFragment : Fragment(R.layout.fragment_paid_amount) {

    lateinit var binding: FragmentPaidAmountBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentPaidAmountBinding.bind(view)

        binding.paidAmountToolbar.setNavigationOnClickListener {

            (parentFragment as? AddExpenseIGSheet)?.goBack()
        }
    }
}