package com.example.splitwise.fragments.expenses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.splitwise.R
import com.example.splitwise.adapters.PaidByAdapter
import com.example.splitwise.databinding.FragmentPaidByBinding
import com.example.splitwise.viewModels.GroupViewModel
import kotlinx.coroutines.launch

class PaidByFragment : Fragment(R.layout.fragment_paid_by) {

    lateinit var binding: FragmentPaidByBinding
    lateinit var paidByAdapter: PaidByAdapter
    val args: PaidByFragmentArgs by navArgs()
    val groupViewModel: GroupViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentPaidByBinding.bind(view)
        setUpRecyclerView()
        observeUser()
        handleClicks()
    }

    fun handleClicks(){

        binding.multiplePeople.setOnClickListener {
            findNavController().navigate(R.id.action_paidByFragment2_to_paidAmountFragment)
        }
        binding.whoPaidToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    fun observeUser(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                groupViewModel.groupMembers.collect { users->
                    paidByAdapter.submitList(users)
                }
            }
        }
    }

    fun setUpRecyclerView(){
        paidByAdapter = PaidByAdapter { user ->

            parentFragmentManager.setFragmentResult(
                "paidByResult",
                Bundle().apply {
                    putString("userId", user.id)
                    putString("userName", user.name)
                }
            )

            findNavController().popBackStack()
        }
        binding.paidByWhoRv.apply {
            layoutManager= LinearLayoutManager(requireContext())
            adapter= this@PaidByFragment.paidByAdapter
        }

    }


}