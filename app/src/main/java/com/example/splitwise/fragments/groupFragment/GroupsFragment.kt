package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.adapters.GroupAdapter
import com.example.splitwise.databinding.ActivityMainBinding
import com.example.splitwise.databinding.FragmentGroupsBinding
import com.example.splitwise.fragments.expenses.AddExpenseIGSheet
import com.example.splitwise.viewModels.GroupViewModel
import kotlinx.coroutines.launch

class GroupsFragment : Fragment(R.layout.fragment_groups) {
    lateinit var binding: FragmentGroupsBinding
    lateinit var groupAdapter: GroupAdapter
    val groupViewModel: GroupViewModel by activityViewModels()

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
        groupViewModel.getGroupsWithBalances()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding= FragmentGroupsBinding.bind(view)

        handleClicks()
        handleToolbar()
        moveToDetails()
        observeGroups()
        setUpRecyclerView()
    }

    fun handleClicks(){
        val mainActivity = requireActivity() as MainActivity

//        binding.groupsScrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
//
//            if (scrollY > oldScrollY) {
//                mainActivity.shrinkAddExpenseButton()
//            } else if (scrollY < oldScrollY) {
//                mainActivity.extendAddExpenseButton()
//            }
//        }

        binding.nonGroupExpensesCL.setOnClickListener {
            findNavController().navigate(R.id.action_groupsFragment_to_detailGroupFragment)
        }
        binding.startAGroupBtn.setOnClickListener {
            openBottomSheet()
        }
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

    fun moveToDetails(){
        parentFragmentManager.setFragmentResultListener(
            "group_created",
            viewLifecycleOwner
        ) { _, bundle ->

            val groupId =
                bundle.getString("groupId")
                    ?: return@setFragmentResultListener

            val action =
                GroupsFragmentDirections.actionGroupsFragmentToDetailGroupFragment(groupId)

            findNavController().navigate(action)
        }
    }

    fun setUpRecyclerView(){
        groupAdapter= GroupAdapter{ group->
            val action= GroupsFragmentDirections.
            actionGroupsFragmentToDetailGroupFragment(
                 group.id)
            findNavController().navigate(action)

        }

        binding.groupRv.apply {
            layoutManager= LinearLayoutManager(requireContext())
            adapter= this@GroupsFragment.groupAdapter
        }
    }

    fun observeGroups() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                groupViewModel.groupsWithBalances.collect { groups ->
                    groupAdapter.submitList(groups)
                }
            }
        }
    }
}