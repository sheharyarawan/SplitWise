package com.example.splitwise.ui.fragments.friendFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splitwise.ui.MainActivity
import com.example.splitwise.R
import com.example.splitwise.ui.adapters.FriendsAdapter
import com.example.splitwise.databinding.FragmentFriendsBinding
import com.example.splitwise.ui.fragments.expenses.AddExpenseIGSheet
import com.example.splitwise.ui.viewModels.FriendViewModel
import kotlinx.coroutines.launch


class FriendsFragment : Fragment(R.layout.fragment_friends) {

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.showAddExpenseButton()
        mainActivity.showBottomNav()
        mainActivity.setAddExpenseClickListener {
            if (isAdded) {
                openBottomSheetExpense()
            }
        }
    }
    lateinit var binding: FragmentFriendsBinding
    lateinit var friendAdapter: FriendsAdapter
    val friendViewModel: FriendViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentFriendsBinding.bind(view)
        binding.addMoreFriendButton.setOnClickListener {
            val action =
                FriendsFragmentDirections
                    .actionFriendsFragmentToAddFriendFragment(
                        source = "friends",
                        groupId = null
                    )

            findNavController().navigate(action)
        }
        setUpToolbarClicks()
        setUpRecyclerView()
        observeFriends()
    }

    fun setUpToolbarClicks(){
        binding.friendsToolbar.setOnMenuItemClickListener { item->
            when(item.itemId) {
                R.id.friendSearch -> {
                true
            }
                R.id.friendAdd->{
                    val action =
                        FriendsFragmentDirections
                            .actionFriendsFragmentToAddFriendFragment(
                                source = "friends",
                                groupId = null
                            )

                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }
    }

    fun observeFriends(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                friendViewModel.friends.collect { friend->
                    friendAdapter.submitList(friend)
                }
            }
        }
    }

    fun setUpRecyclerView(){
        friendAdapter= FriendsAdapter()
        binding.friendRv.apply {
            layoutManager= LinearLayoutManager(requireContext())
            adapter= this@FriendsFragment.friendAdapter
        }
    }

    fun openBottomSheetExpense(){

        AddExpenseIGSheet().show(
            parentFragmentManager,"AddExpenseBottomSheet"
        )
    }
}