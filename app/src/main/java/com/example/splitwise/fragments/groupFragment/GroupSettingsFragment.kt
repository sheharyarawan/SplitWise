package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.adapters.GroupMemberAdapter
import com.example.splitwise.databinding.FragmentGroupSettingsBinding
import com.example.splitwise.viewModels.GroupViewModel
import kotlinx.coroutines.launch

class GroupSettingsFragment : Fragment(R.layout.fragment_group_settings) {

    private lateinit var binding: FragmentGroupSettingsBinding
    lateinit var memberAdapter: GroupMemberAdapter
    val groupViewModel: GroupViewModel by activityViewModels()

    val args: GroupSettingsFragmentArgs by navArgs()
    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.hideAddExpenseButton()
        mainActivity.hideBottomNav()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGroupSettingsBinding.bind(view)

        setUpRecyclerView()
        observeGroupDetails()
        observeGroupMembers()

        handleClicks()
    }

    fun handleClicks(){
        binding.addPeopleToGroup.setOnClickListener {
            val action= GroupSettingsFragmentDirections
                .actionGroupSettingsFragmentToAddFriendFragment(args.groupId)
            findNavController().navigate(action)
        }
        binding.toolbarGroupSettings.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.customizeGroupCL.setOnClickListener {
            openBottomSheet()
        }
    }
    fun openBottomSheet(){

        AddMemberBottomSheet().show(
            parentFragmentManager,"AddBottomSheet"
        )
    }

    fun observeGroupDetails(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED){
                    groupViewModel.group.collect { group ->
                        binding.groupName.text= group?.name
                        binding.groupType.text= group?.type
                    }
                }
        }
    }

    fun observeGroupMembers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED){
                groupViewModel.groupMembers.collect { members ->
                    memberAdapter.submitList(members)
                }
            }
        }
    }



    fun setUpRecyclerView(){
        memberAdapter= GroupMemberAdapter()
        binding.detailGroupSettingsRv.apply {
            layoutManager= LinearLayoutManager(requireContext())
            adapter= memberAdapter
        }
    }
}