package com.example.splitwise.ui.fragments.expenses

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAddExpenseBinding
import com.example.splitwise.data.model.Expense
import com.example.splitwise.data.model.User
import com.example.splitwise.utils.SplitCalculator
import com.example.splitwise.viewModels.ExpenseViewModel
import com.google.firebase.auth.FirebaseAuth

class AddExpenseFragment : Fragment(R.layout.fragment_add_expense) {

    lateinit var binding: FragmentAddExpenseBinding
    val args: AddExpenseFragmentArgs by navArgs()
    val expenseViewModel: ExpenseViewModel by activityViewModels()
    private var userId: String? = FirebaseAuth.getInstance().currentUser?.uid

    lateinit var userName:String
    private var splitType: String = "equally"

    private var splitUsers: List<User> = emptyList()

    private var unequallyAmounts: Map<String, Double> = emptyMap()

    private var percentageValues: Map<String, Double> = emptyMap()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddExpenseBinding.bind(view)

        handleClicks()
        getDataFromPaidBy()
        getDataFromSplit()
    }

    fun handleClicks() {
        binding.IgToolbar.setNavigationOnClickListener {
            val bottomSheet =
                requireParentFragment().requireParentFragment() as AddExpenseIGSheet

            bottomSheet.dismiss()
        }
        binding.IgToolbar.setOnMenuItemClickListener{
            addExpense()

            true
        }

        binding.paidByAndSplitFor2.setOnClickListener {
            findNavController().navigate(R.id.action_addExpenseFragment_to_expenseSplitFragment)
        }

        binding.expensePaidBy.setOnClickListener {

            val action =
                AddExpenseFragmentDirections.actionAddExpenseFragmentToPaidByFragment2(args.groupId)
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

            userId = bundle.getString("userId")
            userName = bundle.getString("userName").toString()

            binding.expensePaidBy.text = userName
        }
    }

    fun getDataFromSplit() {
        parentFragmentManager.setFragmentResultListener(
            "splitResult",
            viewLifecycleOwner
        ) { _, bundle ->

            splitType = bundle.getString("splitType") ?: "equally"

            when (splitType) {

                "equally" -> {
                    splitUsers =
                        BundleCompat.getParcelableArrayList(
                            bundle,
                            "selectedUsers",
                            User::class.java
                        ) ?: emptyList()

                    binding.expenseSplit.text = "equally"

                }

                "unequally" -> {

                    val userIds =
                        bundle.getStringArray("userIds")
                            ?: emptyArray()

                    val amounts =
                        bundle.getDoubleArray("amounts")
                            ?: doubleArrayOf()

                    unequallyAmounts =
                        userIds.zip(amounts.toList()).toMap()

                    binding.expenseSplit.text = "unequally"
                }

                "by percentage" -> {

                    val userIds =
                        bundle.getStringArray("userIds")
                            ?: emptyArray()

                    val percentages =
                        bundle.getDoubleArray("percentages")
                            ?: doubleArrayOf()

                    percentageValues =
                        userIds.zip(percentages.toList()).toMap()

                    binding.expenseSplit.text = "by percentage"
                }
            }
        }
    }

    fun addExpense() {
        val groupId = args.groupId
        val description =
            binding.expenseDescriptionEt.text
                .toString()
                .trim()

        val amount =
            binding.expenseTotalEt.text
                .toString()
                .toDoubleOrNull()

        if (description.isBlank()) {
            Toast.makeText(
                requireContext(),
                "DescriptionEmpty", Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (amount == null || amount <= 0) {
            Toast.makeText(
                requireContext(),
                "Enter a valid amount", Toast.LENGTH_SHORT
            ).show()
            return
        }

        val paidBy = userId
        val splitType = binding.expenseSplit.text.toString()
        val involvedUsers = mutableListOf<String>()

        splitUsers.forEach { user ->
            involvedUsers.add(user.id)
        }

        val splits =
            when (splitType) {

                "equally" -> {

                    SplitCalculator.calculateEqualSplit(
                        amount = amount,
                        users = splitUsers
                    )
                }

                "unequally" -> {

                    SplitCalculator.calculateUnequalSplit(
                        amounts = unequallyAmounts
                    )
                }

                "by percentage" -> {

                    SplitCalculator.calculatePercentageSplit(
                        amount = amount,
                        percentages = percentageValues
                    )
                }

                else -> {
                    emptyList()
                }
            }

        val expense = Expense(
            groupId = groupId,
            description = description,
            amount = amount,
            paidByName = userName,
            paidBy = paidBy,
            involvedUserIds = involvedUsers,
            splitType = splitType,
            splits = splits
        )

        expenseViewModel.addExpense(expense,
            onSuccess = {
                val bottomSheet =
                    requireParentFragment().requireParentFragment() as AddExpenseIGSheet

                bottomSheet.dismiss() },
            onFailure = {exception ->
                exception.message
            })
    }
}
