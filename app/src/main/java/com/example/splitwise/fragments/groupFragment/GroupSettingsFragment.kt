package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
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

class GroupSettingsFragment :
    Fragment(R.layout.fragment_group_settings) {

    private lateinit var binding: FragmentGroupSettingsBinding

    private lateinit var memberAdapter: GroupMemberAdapter

    val groupViewModel: GroupViewModel by activityViewModels()

    val args: GroupSettingsFragmentArgs by navArgs()


    override fun onResume() {
        super.onResume()

        val mainActivity =
            requireActivity() as MainActivity

        mainActivity.hideAddExpenseButton()
        mainActivity.hideBottomNav()


        /*
         * Refresh balances whenever we return to
         * Group Settings.
         *
         * This is especially useful after a settlement.
         */
        groupViewModel.getGroupBalances(
            groupId = args.groupId,

            onFailure = { exception ->
                Log.e(
                    "GroupSettingsFragment",
                    "Failed to refresh group balances",
                    exception
                )
            }
        )
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )

        binding =
            FragmentGroupSettingsBinding.bind(view)


        setUpRecyclerView()

        /*
         * Load the latest group.
         */
        groupViewModel.getGroupById(
            args.groupId
        )


        /*
         * Load members.
         *
         * We need members because GroupBalance only
         * contains user IDs. GroupMemberBalance also
         * contains name/email for the adapter.
         */
        groupViewModel.getGroupMembers(
            groupId = args.groupId,

            onFailure = { exception ->
                Log.e(
                    "GroupSettingsFragment",
                    "Failed to load group members",
                    exception
                )
            }
        )


        /*
         * Load balances.
         */
        groupViewModel.getGroupBalances(
            groupId = args.groupId,

            onFailure = { exception ->
                Log.e(
                    "GroupSettingsFragment",
                    "Failed to load group balances",
                    exception
                )
            }
        )


        observeGroupDetails()

        observeGroupMembers()

        observeGroupBalances()

        handleClicks()
    }


    private fun handleClicks() {

        binding.addPeopleToGroup.setOnClickListener {

            val action =
                GroupSettingsFragmentDirections
                    .actionGroupSettingsFragmentToAddFriendFragment(
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

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                groupViewModel.group.collect { group ->

                    binding.groupName.text =
                        group?.name

                    binding.groupType.text =
                        group?.type
                }
            }
        }
    }


    private fun observeGroupMembers() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                groupViewModel.groupMembers.collect {

                    /*
                     * Members are loaded first/independently.
                     *
                     * Recalculate GroupMemberBalance using
                     * the already loaded GroupBalance list.
                     */
                    groupViewModel.getGroupBalances(
                        groupId = args.groupId,

                        onFailure = { exception ->
                            Log.e(
                                "GroupSettingsFragment",
                                "Failed to refresh balances after members loaded",
                                exception
                            )
                        }
                    )
                }
            }
        }
    }


    private fun observeGroupBalances() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                groupViewModel.groupBalances.collect {

                    /*
                     * getGroupBalances() in ViewModel
                     * converts these into memberBalances.
                     *
                     * GroupMemberAdapter continues to
                     * receive GroupMemberBalance.
                     */
                    observeMemberBalances()
                }
            }
        }
    }


    private fun observeMemberBalances() {

        /*
         * We should not create a new collector every time
         * groupBalances changes.
         *
         * This method is intentionally empty here.
         *
         * memberBalances has its own observer below.
         */
    }


    private fun setUpRecyclerView() {

        memberAdapter =
            GroupMemberAdapter()

        binding.detailGroupSettingsRv.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = memberAdapter
        }


        observeMemberBalanceList()
    }


    private fun observeMemberBalanceList() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                groupViewModel.memberBalances.collect { balances ->

                    memberAdapter.submitList(
                        balances
                    )
                }
            }
        }
    }
}