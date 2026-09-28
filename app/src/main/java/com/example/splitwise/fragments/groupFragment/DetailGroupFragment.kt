package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentDetailGroupBinding
import com.example.splitwise.fragments.expenses.AddExpenseIGSheet
import com.example.splitwise.viewModels.GroupViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailGroupFragment : Fragment(R.layout.fragment_detail_group) {

    lateinit var binding: FragmentDetailGroupBinding
    val args: DetailGroupFragmentArgs by navArgs()
    val groupViewModel : GroupViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()

        val mainActivity = requireActivity() as MainActivity
        mainActivity.showBottomNav()
        mainActivity.showAddExpenseButton()

        mainActivity.setAddExpenseClickListener {
            if (isAdded) {
                openBottomSheet(args.groupId)
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

        handleClicks()
        getGroupDetails()
        observeGroup()
        loadGroupMembers()

    }

    fun openBottomSheet(groupId:String){
        val bottomSheet = AddExpenseIGSheet().apply {
            arguments = Bundle().apply {
                putString("groupId", groupId)
            }
        }

        bottomSheet.show(
            parentFragmentManager,
            "AddExpenseBottomSheet"
        )
    }

    fun handleClicks(){
        binding.detailGroupSettings.setOnClickListener {
            val action =
                DetailGroupFragmentDirections.actionDetailGroupFragmentToGroupSettingsFragment(args.groupId)
            findNavController().navigate(
                action
            )
        }
        binding.detailGroupPeopleCountChip.setOnClickListener {
            findNavController().navigate(R.id.action_detailGroupFragment_to_groupSettingsFragment)
        }
        binding.groupDetailBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.addFriendGroupButton.setOnClickListener {

            val action= DetailGroupFragmentDirections.
            actionDetailGroupFragmentToAddFriendFragment(args.groupId)
            findNavController().navigate(
                action
            )
        }
        binding.friendDetailSettleUpChip.setOnClickListener {
            findNavController().navigate(R.id.action_detailGroupFragment_to_groupSettleFragment)
        }
        binding.balanceChip.setOnClickListener {
            findNavController().navigate(R.id.action_detailGroupFragment_to_balancesFragment)
        }
    }

    fun getGroupDetails(){
        groupViewModel.getGroupById(args.groupId)
    }

    fun observeGroup(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                groupViewModel.group.collect{ group ->
                    binding.detailGroupName.text= group?.name
                    val count=group?.memberIds?.size
                    if (count != null) {
                        if(count>1){
                            binding.detailGroupPeopleCountChip.visibility= View.VISIBLE
                            binding.detailGroupPeopleCountChip.text= "${count} people  +"
                            binding.singleMemberLinearDisplay.visibility= View.VISIBLE
                            binding.AddGroupMemberLayout.visibility= View.GONE
                        }
                        else{
                            binding.detailGroupPeopleCountChip.visibility= View.GONE
                            binding.AddGroupMemberLayout.visibility= View.VISIBLE
                            binding.singleMemberLinearDisplay.visibility= View.GONE
                        }
                    }

                }
            }
        }
    }

    fun loadGroupMembers(){
        groupViewModel.getGroupMembers(args.groupId, onFailure = {
            Toast.makeText(requireContext()
                ,"Error", Toast.LENGTH_SHORT).show()
        })
    }


}