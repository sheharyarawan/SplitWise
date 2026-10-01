package com.example.splitwise.ui.fragments.groupFragment
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
import com.example.splitwise.ui.MainActivity
import com.example.splitwise.R
import com.example.splitwise.ui.adapters.GroupMemberAdapter
import com.example.splitwise.databinding.FragmentGroupSettingsBinding
import com.example.splitwise.ui.viewModels.GroupViewModel
import com.example.splitwise.utils.DialogUtils
import com.example.splitwise.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class GroupSettingsFragment : Fragment(R.layout.fragment_group_settings) {
    private lateinit var binding: FragmentGroupSettingsBinding
    private lateinit var memberAdapter: GroupMemberAdapter
    private val groupViewModel: GroupViewModel by activityViewModels()
    private val args: GroupSettingsFragmentArgs by navArgs()
    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.hideAddExpenseButton()
        mainActivity.hideBottomNav()
        groupViewModel.getGroupBalances(args.groupId)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroupSettingsBinding.bind(view)
        setUpRecyclerView()
        observeGroupDetails()
        observeGroupMembers()
        observeGroupBalances()
        observeMemberBalances()
        handleClicks()
        groupViewModel.getGroupById(args.groupId)
        groupViewModel.getGroupMembers(args.groupId)
        groupViewModel.getGroupBalances(args.groupId)
    }
    private fun handleClicks() {
        binding.addPeopleToGroup.setOnClickListener {
            val action = GroupSettingsFragmentDirections.actionGroupSettingsFragmentToAddFriendFragment(
                "groups",
                args.groupId
            )
            findNavController().navigate(action)
        }
        binding.toolbarGroupSettings.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.customizeGroupCL.setOnClickListener {
            openBottomSheet()
        }
    }
    private fun openBottomSheet() {
        AddMemberBottomSheet().show(
            parentFragmentManager,
            "AddBottomSheet"
        )
    }
    private fun observeGroupDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupViewModel.group.collect { state ->
                    when (state) {
                        UiState.Loading -> {
                            binding.groupSettingsProgressBar.isVisible = true
                        }
                        is UiState.Success -> {
                            binding.groupSettingsProgressBar.isVisible = false
                            binding.groupName.text = state.data.name
                            binding.groupType.text = state.data.type
                        }
                        is UiState.Error -> {
                            binding.groupSettingsProgressBar.isVisible = false
                            DialogUtils.showErrorDialog(
                                requireContext(),
                                state.message
                            )
                        }
                    }
                }
            }
        }
    }
    private fun observeGroupMembers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupViewModel.groupMembers.collect { state ->
                    when (state) {
                        UiState.Loading -> {
                            binding.groupSettingsProgressBar.isVisible = true
                        }
                        is UiState.Success -> {
                            binding.groupSettingsProgressBar.isVisible = false
                        }
                        is UiState.Error -> {
                            binding.groupSettingsProgressBar.isVisible = false
                            DialogUtils.showErrorDialog(
                                requireContext(),
                                state.message
                            )
                        }
                    }
                }
            }
        }
    }
    private fun observeGroupBalances() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupViewModel.groupBalances.collect { state ->
                    when (state) {
                        UiState.Loading -> {
                            binding.groupSettingsProgressBar.isVisible = true
                        }
                        is UiState.Success -> {
                            binding.groupSettingsProgressBar.isVisible = false
                        }
                        is UiState.Error -> {
                            binding.groupSettingsProgressBar.isVisible = false
                            DialogUtils.showErrorDialog(
                                requireContext(),
                                state.message
                            )
                        }
                    }
                }
            }
        }
    }
    private fun observeMemberBalances() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupViewModel.memberBalances.collect { state ->
                    when (state) {
                        UiState.Loading -> {
                            binding.groupSettingsProgressBar.isVisible = true
                        }
                        is UiState.Success -> {
                            binding.groupSettingsProgressBar.isVisible = false
                            memberAdapter.submitList(state.data)
                        }
                        is UiState.Error -> {
                            binding.groupSettingsProgressBar.isVisible = false
                            DialogUtils.showErrorDialog(
                                requireContext(),
                                state.message
                            )
                        }
                    }
                }
            }
        }
    }
    private fun setUpRecyclerView() {
        memberAdapter = GroupMemberAdapter()
        binding.detailGroupSettingsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = memberAdapter
        }
    }
}
