package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentBalancesBinding

class BalancesFragment : Fragment(R.layout.fragment_balances) {

    lateinit var binding: FragmentBalancesBinding

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.hideBottomNav()
        mainActivity.hideAddExpenseButton()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentBalancesBinding.bind(view)
        handleClicks()
    }

    fun handleClicks(){
        binding.groupBalancesToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}