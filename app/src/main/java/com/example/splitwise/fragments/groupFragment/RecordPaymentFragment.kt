package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentRecordPaymentBinding


class RecordPaymentFragment : Fragment(R.layout.fragment_record_payment) {
    lateinit var binding: FragmentRecordPaymentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentRecordPaymentBinding.bind(view)
        handleClicks()
    }

    fun handleClicks(){
        findNavController().navigateUp()
    }
}