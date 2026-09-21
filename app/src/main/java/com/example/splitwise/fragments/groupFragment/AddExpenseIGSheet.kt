package com.example.splitwise.fragments.groupFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.room.util.query
import com.example.splitwise.R
import com.example.splitwise.adapters.AddExpenseViewPager
import com.example.splitwise.databinding.AddExpenseBottomLayoutBinding
import com.example.splitwise.databinding.CreateAGroupBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddExpenseIGSheet: BottomSheetDialogFragment() {
    lateinit var binding: AddExpenseBottomLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddExpenseBottomLayoutBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val bottomSheet = dialog?.findViewById<View>(
            com.google.android.material.R.id.design_bottom_sheet
        )

        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT

        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager()
    }

    fun setUpViewPager() {
        binding.addExpenseVp.adapter = AddExpenseViewPager(this)
            binding.addExpenseVp.currentItem=0
    }
}