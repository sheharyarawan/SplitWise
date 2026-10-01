package com.example.splitwise.ui.fragments.expenses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.adapters.SplitViewPager
import com.example.splitwise.databinding.FragmentSplitBinding
import com.example.splitwise.viewModels.SplitViewModel
import com.google.android.material.tabs.TabLayoutMediator

class SplitFragment : Fragment(R.layout.fragment_split) {

    private lateinit var binding: FragmentSplitBinding

    private val splitViewModel: SplitViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSplitBinding.bind(view)

        setUpViewPager()
        handleClicks()
    }

    private fun setUpViewPager() {

        binding.splitViewPager.adapter = SplitViewPager(this)

        TabLayoutMediator(
            binding.splitTabsLayout,
            binding.splitViewPager
        ) { tab, position ->

            when (position) {

                0 -> tab.text = "Equally"

                1 -> tab.text = "Unequally"

                2 -> tab.text = "By percentage"
            }

        }.attach()
    }

    private fun handleClicks() {

        binding.splitToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.splitToolbar.setOnMenuItemClickListener { item ->

            when (item.itemId) {

                R.id.addFriendToolbarCheck -> {

                    sendSplitResult()

                    true
                }

                else -> false
            }
        }
    }

    private fun sendSplitResult() {

        when (binding.splitViewPager.currentItem) {

            0 -> {

                val selectedUsers =
                    splitViewModel.equallyUsers

                parentFragmentManager.setFragmentResult(
                    "splitResult",
                    Bundle().apply {

                        putString(
                            "splitType",
                            "equally"
                        )

                        putParcelableArrayList(
                            "selectedUsers",
                            ArrayList(selectedUsers)
                        )
                    }
                )

                findNavController().navigateUp()
            }

            1 -> {

                val amounts =
                    splitViewModel.unequallyAmounts

                parentFragmentManager.setFragmentResult(
                    "splitResult",
                    Bundle().apply {

                        putString(
                            "splitType",
                            "unequally"
                        )

                        putStringArray(
                            "userIds",
                            amounts.keys.toTypedArray()
                        )

                        putDoubleArray(
                            "amounts",
                            amounts.values.toDoubleArray()
                        )
                    }
                )

                findNavController().navigateUp()
            }

            2 -> {

                val percentages =
                    splitViewModel.percentageValues

                parentFragmentManager.setFragmentResult(
                    "splitResult",
                    Bundle().apply {

                        putString(
                            "splitType",
                            "by percentage"
                        )

                        putStringArray(
                            "userIds",
                            percentages.keys.toTypedArray()
                        )

                        putDoubleArray(
                            "percentages",
                            percentages.values.toDoubleArray()
                        )
                    }
                )

                findNavController().navigateUp()
            }
        }
    }
}