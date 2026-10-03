package com.example.splitwise.ui.fragments.authFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentLoginBinding
import com.example.splitwise.ui.MainActivity
import com.example.splitwise.ui.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentLoginBinding.bind(view)
        setUpClicks()
    }
    fun setUpClicks(){
        binding.apply {
            forgotPasswordText.setOnClickListener {
            }
            loginButton.setOnClickListener {
                login()
            }
            loginToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
    private fun login() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        viewModel.signInWithEmail(
            email = email,
            password = password,
            onSuccess = {
                (requireActivity() as MainActivity).showMainApp()
            },
            onFailure = { message ->
                binding.emailInputLayout.error = null
                binding.passwordInputLayout.error = null
                if (message.contains("email", ignoreCase = true)) {
                    binding.emailInputLayout.error = message
                } else {
                    binding.passwordInputLayout.error = message
                }
            }
        )
    }
}