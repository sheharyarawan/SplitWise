package com.example.splitwise.ui.fragments.expenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.splitwise.R
import com.example.splitwise.databinding.AddExpenseBottomLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddExpenseIGSheet : BottomSheetDialogFragment() {

    lateinit var binding: AddExpenseBottomLayoutBinding

    private val groupId: String?
        get() = arguments?.getString("groupId")

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(
                R.id.fragmentContainerBottomSheet
            ) as NavHostFragment

        val navController = navHostFragment.navController

        navController.setGraph(
            R.navigation.nav_graph_bottom,
            Bundle().apply {
                putString("groupId", groupId)
            }
        )
    }

    override fun onStart() {
        super.onStart()

        val bottomSheet = dialog?.findViewById<View>(
            com.google.android.material.R.id.design_bottom_sheet
        )

        bottomSheet?.layoutParams?.height =
            ViewGroup.LayoutParams.MATCH_PARENT

        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }
}