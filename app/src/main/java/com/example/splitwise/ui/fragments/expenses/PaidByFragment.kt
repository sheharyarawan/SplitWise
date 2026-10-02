package com.example.splitwise.ui.fragments.expenses
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splitwise.R
import com.example.splitwise.ui.adapters.PaidByAdapter
import com.example.splitwise.databinding.FragmentPaidByBinding
import com.example.splitwise.ui.viewModels.GroupViewModel
import com.example.splitwise.utils.DialogUtils
import com.example.splitwise.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class PaidByFragment : Fragment(R.layout.fragment_paid_by) {
    private lateinit var binding: FragmentPaidByBinding
    private lateinit var paidByAdapter: PaidByAdapter
    private val args: PaidByFragmentArgs by navArgs()
    private val groupViewModel: GroupViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaidByBinding.bind(view)
        setUpRecyclerView()
        observeUsers()
        handleClicks()
    }
    private fun handleClicks() {
        binding.multiplePeople.setOnClickListener {
            findNavController().navigate(R.id.action_paidByFragment2_to_paidAmountFragment)
        }
        binding.whoPaidToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
    private fun observeUsers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupViewModel.groupMembers.collect { state ->
                    when (state) {
                        UiState.Loading -> {
                            binding.paidByProgressBar.isVisible = true
                        }
                        is UiState.Success -> {
                            binding.paidByProgressBar.isVisible = false
                            paidByAdapter.submitList(state.data)
                        }
                        is UiState.Error -> {
                            binding.paidByProgressBar.isVisible = false
                            DialogUtils.showErrorDialog(requireContext(), state.message)
                        }
                    }
                }
            }
        }
    }
    private fun setUpRecyclerView() {
        paidByAdapter = PaidByAdapter { user ->
            parentFragmentManager.setFragmentResult(
                "paidByResult",
                Bundle().apply {
                    putString("userId", user.id)
                    putString("userName", user.name)
                }
            )
            findNavController().popBackStack()
        }
        binding.paidByWhoRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@PaidByFragment.paidByAdapter
        }
    }
}