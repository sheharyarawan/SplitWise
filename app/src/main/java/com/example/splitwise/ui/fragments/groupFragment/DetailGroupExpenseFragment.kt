package com.example.splitwise.ui.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splitwise.R
import com.example.splitwise.ui.adapters.DetailGroupExpenseAdapter
import com.example.splitwise.databinding.FragmentDetailGroupExpenseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailGroupExpenseFragment : Fragment(R.layout.fragment_detail_group_expense) {

    val args: DetailGroupExpenseFragmentArgs by navArgs()
    lateinit var binding: FragmentDetailGroupExpenseBinding
    lateinit var detailGroupExpenseAdapter: DetailGroupExpenseAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentDetailGroupExpenseBinding.bind(view)
        updateUi()
        setUpRecyclerView()
        setUpClicks()
    }

    fun setUpClicks(){
        binding.toolBarFragmentDetailExpense.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    fun updateUi(){
        binding.expenseDescription.text= args.expense.description
        binding.expenseAmount.text= "Rs ${args.expense.amount}"
        binding.expenseTimeDate.text= "Added by ${args.expense.paidByName} on ${args.expense.date}"
        binding.expensePaidBy.text= "${args.expense.paidByName} paid Rs ${args.expense.amount}"
    }

    fun giveData(){
        detailGroupExpenseAdapter.submitList(args.expense.splits)
    }

    fun setUpRecyclerView(){
        detailGroupExpenseAdapter= DetailGroupExpenseAdapter()
        detailGroupExpenseAdapter.submitList(args.expense.splits)
        binding.expenseDistributionRv.apply {
            layoutManager= LinearLayoutManager(requireContext())
            adapter= this@DetailGroupExpenseFragment.detailGroupExpenseAdapter
        }
    }
}