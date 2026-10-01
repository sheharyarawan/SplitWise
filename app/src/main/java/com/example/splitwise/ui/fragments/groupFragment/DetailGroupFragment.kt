package com.example.splitwise.ui.fragments.groupFragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splitwise.ui.MainActivity
import com.example.splitwise.R
import com.example.splitwise.ui.adapters.GroupExpensesAdapter
import com.example.splitwise.databinding.FragmentDetailGroupBinding
import com.example.splitwise.data.model.Expense
import com.example.splitwise.data.model.GroupBalance
import com.example.splitwise.ui.fragments.expenses.AddExpenseIGSheet
import com.example.splitwise.utils.GroupBalanceCalculator
import com.example.splitwise.ui.viewModels.ExpenseViewModel
import com.example.splitwise.ui.viewModels.GroupViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class DetailGroupFragment : Fragment(R.layout.fragment_detail_group) {

    lateinit var binding: FragmentDetailGroupBinding
    val args: DetailGroupFragmentArgs by navArgs()
    val groupViewModel : GroupViewModel by activityViewModels()
    val expenseViewModel: ExpenseViewModel by activityViewModels()
    lateinit var groupExpenseAdapter: GroupExpensesAdapter

    override fun onResume() {
        super.onResume()

        val mainActivity = requireActivity() as MainActivity
        mainActivity.showBottomNav()
        mainActivity.showAddExpenseButton()

        mainActivity.setAddExpenseClickListener {
            if (isAdded) {
                openBottomSheet(args.groupId)
            }
        }
        groupViewModel.getGroupBalances(
            groupId = args.groupId,
            onFailure = { exception ->
                Log.e(
                    "DetailGroupFragment",
                    "Failed to refresh balances",
                    exception
                )
            }
        )
    }

    override fun onPause() {
        super.onPause()

        (requireActivity() as MainActivity)
            .clearAddExpenseClickListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentDetailGroupBinding.bind(view)

        handleClicks()
        getGroupDetails()
        observeGroup()
        loadGroupMembers()
        setUpRecyclerView()
        loadExpenses()
        observeExpenses()
        loadGroupBalances()
        observeGroupBalances()
    }

    fun openBottomSheet(groupId:String){
        val bottomSheet = AddExpenseIGSheet().apply {
            arguments = Bundle().apply {
                putString("groupId", groupId)
            }
        }

        bottomSheet.show(
            parentFragmentManager,
            "AddExpenseBottomSheet"
        )
    }

    fun handleClicks(){
        binding.detailGroupSettings.setOnClickListener {
            val action =
                DetailGroupFragmentDirections.actionDetailGroupFragmentToGroupSettingsFragment(args.groupId)
            findNavController().navigate(
                action
            )
        }
        binding.detailGroupPeopleCountChip.setOnClickListener {
            findNavController().navigate(R.id.action_detailGroupFragment_to_groupSettingsFragment)
        }
        binding.groupDetailBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.addFriendGroupButton.setOnClickListener {

            val action =
                DetailGroupFragmentDirections
                    .actionDetailGroupFragmentToAddFriendFragment(
                        source = "groups",
                        groupId = args.groupId
                    )

            findNavController().navigate(action)
        }
        binding.friendDetailSettleUpChip.setOnClickListener {
            findNavController().navigate(R.id.action_detailGroupFragment_to_groupSettleFragment)
        }
        binding.balanceChip.setOnClickListener {
            findNavController().navigate(R.id.action_detailGroupFragment_to_balancesFragment)
        }
    }

    fun getGroupDetails(){
        groupViewModel.getGroupById(args.groupId)
    }

    fun observeGroup() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupViewModel.group.collect { group ->

                    binding.detailGroupName.text = group?.name

                    val count = group?.memberIds?.size ?: 0

                    binding.detailGroupPeopleCountChip.isVisible = count > 1

                    if (count > 1) {
                        binding.detailGroupPeopleCountChip.text =
                            "$count people  +"
                    }

                    updateExpenseVisibility(
                        expenseViewModel.groupExpenses.value
                    )
                }
            }
        }
    }

    fun loadGroupMembers(){
        groupViewModel.getGroupMembers(args.groupId, onFailure = {
            Toast.makeText(requireContext()
                ,"Error", Toast.LENGTH_SHORT).show()
        })
    }

    fun loadExpenses(){
        expenseViewModel.getGroupExpenses(args.groupId, onFailure = {

        })
    }

    fun observeExpenses() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                expenseViewModel.groupExpenses.collect { expenses ->
                    groupExpenseAdapter.submitList(expenses)
                    updateExpenseVisibility(expenses)
                }
            }
        }
    }
    fun loadGroupBalances() {

        groupViewModel.getGroupBalances(
            groupId = args.groupId,
            onFailure = { exception ->

                Log.e(
                    "DetailGroupFragment",
                    "Failed to load group balances",
                    exception
                )
            }
        )
    }

    fun observeGroupBalances() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                groupViewModel.groupBalances.collect { balances ->
                    updateGroupTotal(balances)
                }
            }
        }
    }

    fun setUpRecyclerView(){

        groupExpenseAdapter= GroupExpensesAdapter{ expense ->
            val action= DetailGroupFragmentDirections.
            actionDetailGroupFragmentToDetailGroupExpenseFragment(expense)

            findNavController().navigate(action)
        }
        binding.detailGroupPaymentsRv.apply {
            layoutManager= LinearLayoutManager(requireContext())
            adapter= this@DetailGroupFragment.groupExpenseAdapter
        }
    }

    fun updateExpenseVisibility(expenses: List<Expense>) {

        val hasExpenses = expenses.isNotEmpty()
        val memberCount =
            groupViewModel.group.value?.memberIds?.size ?: 0

        binding.detailGroupPaymentsRv.isVisible = hasExpenses

        if (hasExpenses) {
            binding.detailCard.isVisible = false
            binding.singleMemberLinearDisplay.isVisible = false
            return
        }

        binding.detailCard.isVisible = memberCount <= 1
        binding.singleMemberLinearDisplay.isVisible = memberCount > 1
    }

    private fun updateGroupTotal(
        balances: List<GroupBalance>
    ) {

        val currentUserId =
            FirebaseAuth.getInstance().currentUser?.uid
                ?: return

        val youGetBack =
            GroupBalanceCalculator.getYouGetBack(
                balances = balances,
                currentUserId = currentUserId
            )

        val youOwe =
            GroupBalanceCalculator.getYouOwe(
                balances = balances,
                currentUserId = currentUserId
            )

        val fullText: String

        val amountText: String

        when {

            youGetBack > 0.01 -> {

                amountText =
                    "Rs ${formatAmount(youGetBack)}"

                fullText =
                    "You are owed $amountText overall"
            }

            youOwe > 0.01 -> {

                amountText =
                    "Rs ${formatAmount(youOwe)}"

                fullText =
                    "You owe $amountText overall"
            }

            else -> {

                binding.detailGroupExpenseTotalTv.text =
                    "You are settled up"

                return
            }
        }

        val spannable =
            SpannableString(fullText)

        val start =
            fullText.indexOf(amountText)

        val end =
            start + amountText.length

        val color =
            if (youGetBack > 0.01) {

                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )

            } else {

                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            }

        spannable.setSpan(
            ForegroundColorSpan(color),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.detailGroupExpenseTotalTv.text =
            spannable
    }

    private fun formatAmount(amount: Double): String {

        return if (amount % 1.0 == 0.0) {
            amount.toInt().toString()
        } else {
            String.format("%.2f", amount)
        }
    }
}