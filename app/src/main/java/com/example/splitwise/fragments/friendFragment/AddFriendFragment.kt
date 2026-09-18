package com.example.splitwise.fragments.friendFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAddFriendBinding


class AddFriendFragment : Fragment(R.layout.fragment_add_friend) {

    lateinit var binding: FragmentAddFriendBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding= FragmentAddFriendBinding.bind(view)
        setUpToolbarClicks()

    }

    fun setUpToolbarClicks(){
        binding.materialToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}