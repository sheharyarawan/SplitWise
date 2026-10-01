package com.example.splitwise.ui.fragments.friendFragment
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
import com.example.splitwise.utils.DialogUtils
import com.example.splitwise.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class FriendsFragment : Fragment(R.layout.fragment_friends) {
    private lateinit var binding: FragmentFriendsBinding
    private lateinit var friendAdapter: FriendsAdapter
    private val friendViewModel: FriendViewModel by activityViewModels()
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFriendsBinding.bind(view)
        binding.addMoreFriendButton.setOnClickListener {
            val action = FriendsFragmentDirections.actionFriendsFragmentToAddFriendFragment(
                source = "friends",
                groupId = null
            )
            findNavController().navigate(action)
        }
        setUpToolbarClicks()
        setUpRecyclerView()
        observeFriends()
    }
    private fun setUpToolbarClicks() {
        binding.friendsToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.friendSearch -> {
                    true
                }
                R.id.friendAdd -> {
                    val action = FriendsFragmentDirections.actionFriendsFragmentToAddFriendFragment(
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
    private fun observeFriends() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                friendViewModel.friends.collect { state ->
                    when (state) {
                        UiState.Loading -> {
                            binding.friendsProgressBar.isVisible = true
                        }
                        is UiState.Success -> {
                            binding.friendsProgressBar.isVisible = false
                            friendAdapter.submitList(state.data)
                        }
                        is UiState.Error -> {
                            binding.friendsProgressBar.isVisible = false
                            DialogUtils.showErrorDialog(
                                requireContext(),
                                state.message
                            )
                        }
                    }
                }
            }
        }
    }
    private fun setUpRecyclerView() {
        friendAdapter = FriendsAdapter()
        binding.friendRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FriendsFragment.friendAdapter
        }
    }
    private fun openBottomSheetExpense() {
        AddExpenseIGSheet().show(
            parentFragmentManager,
            "AddExpenseBottomSheet"
        )
    }
}
