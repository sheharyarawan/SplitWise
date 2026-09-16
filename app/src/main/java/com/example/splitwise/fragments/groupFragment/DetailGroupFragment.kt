package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentDetailGroupBinding


class DetailGroupFragment : Fragment(R.layout.fragment_detail_group) {

    lateinit var binding: FragmentDetailGroupBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentDetailGroupBinding.bind(view)
        binding.detailGroupSettings.setOnClickListener {
            findNavController().navigate(R.id.action_detailGroupFragment_to_groupSettingsFragment)
        }
        binding.detailGroupPeopleCountChip.setOnClickListener {
            findNavController().navigate(R.id.action_detailGroupFragment_to_groupSettingsFragment)
        }
    }

}