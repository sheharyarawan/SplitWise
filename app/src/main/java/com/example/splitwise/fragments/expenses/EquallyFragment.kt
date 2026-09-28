package com.example.splitwise.fragments.expenses

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splitwise.R
import com.example.splitwise.adapters.EquallyAdapter
import com.example.splitwise.databinding.FragmentEquallyBinding
import com.example.splitwise.model.User
import com.example.splitwise.viewModels.GroupViewModel
import dagger.hilt.android.ViewModelLifecycle
import kotlinx.coroutines.launch


class EquallyFragment : Fragment(R.layout.fragment_equally) {

    lateinit var binding: FragmentEquallyBinding
    lateinit var equallyAdapter: EquallyAdapter
    val groupViewModel: GroupViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentEquallyBinding.bind(view)
        observeUsers()
        setUpRecyclerView()
    }

    fun observeUsers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                groupViewModel.groupMembers.collect { user->
                    equallyAdapter.submitList(user)
                }
            }
        }
    }

    fun setUpRecyclerView() {

        equallyAdapter = EquallyAdapter { selectedUsers ->

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

        binding.splitAllCheckBox.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                equallyAdapter.selectAll()
            } else {
                equallyAdapter.deselectAll()
            }
        }

        binding.splitEqualRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = equallyAdapter
        }
    }
    private fun updateSelectedUsers(
        users: List<User>
    ) {

        val count = users.size

        binding.splitMoneyPercentageLeft.text =
            "($count people)"

        users.forEach { user ->
            Log.d("Debug", "${user.id} - ${user.name}")
        }
    }
}