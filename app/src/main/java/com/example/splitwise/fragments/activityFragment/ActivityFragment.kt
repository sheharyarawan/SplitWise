package com.example.splitwise.fragments.activityFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentActivityBinding

class ActivityFragment : Fragment(R.layout.fragment_activity) {

    lateinit var binding: FragmentActivityBinding

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showAddExpenseButton()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentActivityBinding.bind(view)
    }
}