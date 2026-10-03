package com.example.splitwise.ui.fragments.authFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    lateinit var binding: FragmentLoginBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentLoginBinding.bind(view)
    }
    fun setUpClicks(){
        binding.apply {
            forgotPasswordText.setOnClickListener {
            }
        }
    }
}