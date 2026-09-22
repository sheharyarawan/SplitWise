package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentGroupSettingsBinding

class GroupSettingsFragment : Fragment(R.layout.fragment_group_settings) {

    private lateinit var binding: FragmentGroupSettingsBinding
    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.hideAddExpenseButton()
        mainActivity.hideBottomNav()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGroupSettingsBinding.bind(view)
        binding.addPeopleToGroup.setOnClickListener {
            findNavController().navigate(R.id.action_groupSettingsFragment_to_addFriendFragment)
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
}