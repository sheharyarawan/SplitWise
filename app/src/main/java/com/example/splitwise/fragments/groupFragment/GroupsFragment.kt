package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.ActivityMainBinding
import com.example.splitwise.databinding.FragmentGroupsBinding
import com.example.splitwise.fragments.expenses.AddExpenseIGSheet

class GroupsFragment : Fragment(R.layout.fragment_groups) {
    lateinit var binding: FragmentGroupsBinding

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.showBottomNav()
        mainActivity.showAddExpenseButton()
        mainActivity.setAddExpenseClickListener {
            if (isAdded) {
                openBottomSheetExpense()
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding= FragmentGroupsBinding.bind(view)

        val mainActivity = requireActivity() as MainActivity

        binding.groupsScrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->

            if (scrollY > oldScrollY) {
                mainActivity.shrinkAddExpenseButton()
            } else if (scrollY < oldScrollY) {
                mainActivity.extendAddExpenseButton()
            }
        }

        binding.nonGroupExpensesCL.setOnClickListener {
            findNavController().navigate(R.id.action_groupsFragment_to_detailGroupFragment)
        }
        binding.startAGroupBtn.setOnClickListener {
            openBottomSheet()
        }
        handleToolbar()
    }

    fun openBottomSheet(){

            AddMemberBottomSheet().show(
                parentFragmentManager,"AddBottomSheet"
            )
    }
    fun openBottomSheetExpense(){

        AddExpenseIGSheet().show(
            parentFragmentManager,"AddExpenseBottomSheet"
        )
    }

    fun handleToolbar(){
        binding.groupsToolbar.setOnMenuItemClickListener {item->
            when(item.itemId){
                R.id.groupSearch->{
                    true
                }
                R.id.groupAdd->{
                    openBottomSheet()
                    true
                }
                else -> false
            }
        }
    }
}