package com.example.splitwise.fragments.expenses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splitwise.R
import com.example.splitwise.adapters.EquallyAdapter
import com.example.splitwise.databinding.FragmentEquallyBinding
import com.example.splitwise.viewModels.GroupViewModel
import com.example.splitwise.viewModels.SplitViewModel
import kotlinx.coroutines.launch

class EquallyFragment : Fragment(R.layout.fragment_equally) {

    private lateinit var binding: FragmentEquallyBinding
    private lateinit var equallyAdapter: EquallyAdapter

    private val groupViewModel: GroupViewModel by activityViewModels()

    private val splitViewModel: SplitViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEquallyBinding.bind(view)

        setUpRecyclerView()
        observeUsers()
        handleClicks()
    }

    private fun setUpRecyclerView() {

        equallyAdapter = EquallyAdapter { selectedUsers ->

            splitViewModel.equallyUsers = selectedUsers

            binding.splitAllCheckBox.setOnCheckedChangeListener(null)

            binding.splitAllCheckBox.isChecked =
                equallyAdapter.areAllSelected()

            binding.splitAllCheckBox.setOnCheckedChangeListener { _, isChecked ->

                if (isChecked) {
                    equallyAdapter.selectAll()
                } else {
                    equallyAdapter.deselectAll()
                }
            }

            updateSelectedUsers(selectedUsers)
        }

        binding.splitEqualRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = equallyAdapter
        }
    }

    private fun observeUsers() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                groupViewModel.groupMembers.collect { users ->

                    equallyAdapter.submitList(users)
                }
            }
        }
    }

    private fun handleClicks() {

        binding.splitAllCheckBox.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                equallyAdapter.selectAll()
            } else {
                equallyAdapter.deselectAll()
            }
        }
    }

    private fun updateSelectedUsers(users: List<com.example.splitwise.model.User>) {

        binding.splitMoneyPercentageLeft.text =
            "(${users.size} people)"
    }
}