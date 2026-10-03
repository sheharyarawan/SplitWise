package com.example.splitwise.ui.fragments.authFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {
   lateinit var binding: FragmentForgotPasswordBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentForgotPasswordBinding.bind(view)
    }
}