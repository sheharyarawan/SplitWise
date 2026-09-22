package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentGroupSettingsBinding
import com.example.splitwise.databinding.FragmentGroupSettleBinding


class GroupSettleFragment : Fragment(R.layout.fragment_group_settle) {
   lateinit var binding: FragmentGroupSettleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentGroupSettleBinding.bind(view)
        handleClick()
    }

    fun handleClick(){
        binding.groupSettleUpToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}