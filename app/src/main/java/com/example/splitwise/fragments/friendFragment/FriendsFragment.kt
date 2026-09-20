package com.example.splitwise.fragments.friendFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentFriendsBinding


class FriendsFragment : Fragment(R.layout.fragment_friends) {

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).showAddExpenseButton()
    }
    lateinit var binding: FragmentFriendsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentFriendsBinding.bind(view)
        binding.addMoreFriendButton.setOnClickListener {
           // findNavController().navigate(R.id.action_friendsFragment_to_addFriendFragment)
             findNavController().navigate(R.id.action_friendsFragment_to_friendDetailFragment)
        }
        setUpToolbarClicks()
    }

    fun setUpToolbarClicks(){
        binding.friendsToolbar.setOnMenuItemClickListener { item->
            when(item.itemId) {
                R.id.friendSearch -> {
                true
            }
                R.id.friendAdd->{
                    findNavController().navigate(R.id.action_friendsFragment_to_addFriendFragment)
                    true
                }
                else -> false
            }
        }
    }
}