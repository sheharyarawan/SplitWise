package com.example.splitwise.ui.fragments.activityFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.splitwise.ui.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentActivityBinding
import com.example.splitwise.ui.fragments.expenses.AddExpenseIGSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityFragment : Fragment(R.layout.fragment_activity) {

    lateinit var binding: FragmentActivityBinding

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.showAddExpenseButton()
        mainActivity.showBottomNav()
        mainActivity.setAddExpenseClickListener {
            if (isAdded) {
                openBottomSheetExpense()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentActivityBinding.bind(view)
    }

    fun openBottomSheetExpense(){

        AddExpenseIGSheet().show(
            parentFragmentManager,"AddExpenseBottomSheet"
        )
    }
}