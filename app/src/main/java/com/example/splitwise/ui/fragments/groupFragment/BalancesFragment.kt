package com.example.splitwise.ui.fragments.groupFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.splitwise.ui.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentBalancesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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