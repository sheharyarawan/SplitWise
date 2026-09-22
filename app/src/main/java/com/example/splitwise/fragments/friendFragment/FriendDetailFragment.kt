package com.example.splitwise.fragments.friendFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentFriendDetailBinding
import com.example.splitwise.fragments.expenses.AddExpenseIGSheet


class FriendDetailFragment : Fragment(R.layout.fragment_friend_detail) {

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as MainActivity

        mainActivity.showAddExpenseButton()

        mainActivity.setAddExpenseClickListener {
            openBottomSheet()
        }
    }
    lateinit var binding: FragmentFriendDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentFriendDetailBinding.bind(view)
        setUpToolbarClicks()
    }

    fun setUpToolbarClicks(){
        binding.friendDetailSettingsIcon.setOnClickListener {
            findNavController().navigate(R.id.action_friendDetailFragment_to_friendSettingsFragment)
        }
        binding.friendDetailBackIcon.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    fun openBottomSheet(){

        AddExpenseIGSheet().show(
            parentFragmentManager,"AddExpenseBottomSheet"
        )
    }
}