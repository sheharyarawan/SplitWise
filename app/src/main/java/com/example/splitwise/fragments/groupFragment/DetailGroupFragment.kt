package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentDetailGroupBinding
import com.example.splitwise.fragments.expenses.AddExpenseIGSheet


class DetailGroupFragment : Fragment(R.layout.fragment_detail_group) {

    lateinit var binding: FragmentDetailGroupBinding

    override fun onResume() {
        super.onResume()

        val mainActivity = requireActivity() as MainActivity

        mainActivity.showAddExpenseButton()

        mainActivity.setAddExpenseClickListener {
            if (isAdded) {
                openBottomSheet()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        (requireActivity() as MainActivity)
            .clearAddExpenseClickListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentDetailGroupBinding.bind(view)
        binding.detailGroupSettings.setOnClickListener {
            findNavController().navigate(R.id.action_detailGroupFragment_to_groupSettingsFragment)
        }
        binding.detailGroupPeopleCountChip.setOnClickListener {
            findNavController().navigate(R.id.action_detailGroupFragment_to_groupSettingsFragment)
        }
        binding.groupDetailBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.addFriendGroupButton.setOnClickListener {
            findNavController().navigate(R.id.action_detailGroupFragment_to_addFriendFragment)
        }
    }

    fun openBottomSheet(){

        AddExpenseIGSheet().show(
            parentFragmentManager,"AddExpenseBottomSheet"
        )
    }
}