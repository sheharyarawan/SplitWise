package com.example.splitwise.fragments.friendFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentFriendSettingsBinding


class FriendSettingsFragment : Fragment(R.layout.fragment_friend_settings) {

    lateinit var binding: FragmentFriendSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentFriendSettingsBinding.bind(view)
        binding.friendSettingToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}