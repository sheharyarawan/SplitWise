package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.databinding.ActivityMainBinding
import com.example.splitwise.databinding.FragmentGroupsBinding

class GroupsFragment : Fragment(R.layout.fragment_groups) {
    lateinit var binding: FragmentGroupsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding= FragmentGroupsBinding.bind(view)
        binding.nonGroupExpensesCL.setOnClickListener {
            findNavController().navigate(R.id.action_groupsFragment_to_detailGroupFragment)
        }
        binding.startAGroupBtn.setOnClickListener {
            AddMemberBottomSheet().show(
                parentFragmentManager,"AddBottomSheet"
            )

        }
    }
}