package com.example.splitwise.fragments.accountFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAccountBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth


class AccountFragment : Fragment(R.layout.fragment_account) {

    lateinit var binding: FragmentAccountBinding

    override fun onResume() {
        super.onResume()

        (requireActivity() as MainActivity).hideAddExpenseButton()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentAccountBinding.bind(view)

        setUpClicks()

    }

    fun setUpClicks(){
        binding.accountEdit.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_accountSettingsFragment)
        }
        binding.accountLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val activity = requireActivity() as MainActivity

            activity.hideBottomNav()
            activity.hideAddExpenseButton()

            findNavController().setGraph(R.navigation.auth_nav_graph)
        }
    }

}