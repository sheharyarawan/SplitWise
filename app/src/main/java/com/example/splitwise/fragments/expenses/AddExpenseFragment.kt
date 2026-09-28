package com.example.splitwise.fragments.expenses

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAddExpenseBinding
import com.example.splitwise.fragments.friendFragment.AddFriendFragmentArgs

class AddExpenseFragment : Fragment(R.layout.fragment_add_expense) {

    lateinit var binding: FragmentAddExpenseBinding
    val args: AddExpenseFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentAddExpenseBinding.bind(view)

        handleClicks()
        getDataFromPaidBy()
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

            val action= AddExpenseFragmentDirections.actionAddExpenseFragmentToPaidByFragment2(args.groupId)
            findNavController().navigate(action)

        }
        binding.expenseSplit.setOnClickListener {
            findNavController().navigate(R.id.action_addExpenseFragment_to_splitFragment)
        }
    }

    fun getDataFromPaidBy() {
        parentFragmentManager.setFragmentResultListener(
            "paidByResult",
            viewLifecycleOwner
        ) { _, bundle ->

            val userId = bundle.getString("userId")
            val userName = bundle.getString("userName")

            binding.expensePaidBy.text = userName
        }
    }
}