package com.example.splitwise.ui.fragments.authFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentForgotPasswordBinding
import com.example.splitwise.ui.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {
   lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentForgotPasswordBinding.bind(view)
        setUpClicks()
    }
    fun setUpClicks() {
        binding.resetPasswordButton.setOnClickListener {
            resetPassword()
        }
    }
    private fun resetPassword() {
        val email = binding.emailEditText.text.toString()
        viewModel.sendPasswordResetEmail(
            email = email,
            onSuccess = {
                Toast.makeText(
                    requireContext(),
                    "Password reset email sent",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
            },
            onFailure = { message ->
                binding.emailInputLayout.error = message
            }
        )
    }
}