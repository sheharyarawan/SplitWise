package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentGroupSettingsBinding
import com.example.splitwise.viewModels.GroupViewModel
import kotlinx.coroutines.launch

class GroupSettingsFragment : Fragment(R.layout.fragment_group_settings) {

    private lateinit var binding: FragmentGroupSettingsBinding
    val groupViewModel: GroupViewModel by activityViewModels()
    lateinit var groupId: String
    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.hideAddExpenseButton()
        mainActivity.hideBottomNav()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGroupSettingsBinding.bind(view)

        observeGroupDetails()
        handleClicks()
    }

    fun handleClicks(){
        binding.addPeopleToGroup.setOnClickListener {
            val action= GroupSettingsFragmentDirections
                .actionGroupSettingsFragmentToAddFriendFragment(groupId)
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
                        groupId= group?.id.toString()
                    }
                }
        }
    }
}