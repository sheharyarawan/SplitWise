package com.example.splitwise.ui.fragments.authFragments
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAuthBinding
import com.example.splitwise.ui.MainActivity
import com.example.splitwise.ui.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {
    private lateinit var binding: FragmentAuthBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthBinding.bind(view)
        handleButtonClicks()
    }
    private fun handleButtonClicks() {
        binding.apply {
            signInGoogleButton.setOnClickListener {
                signInWithGoogle()
            }
            signUpButton.setOnClickListener {
                findNavController().navigate(
                    R.id.action_authFragment_to_signUpFragment
                )
            }
            loginButton.setOnClickListener {
                findNavController().navigate(R.id.action_authFragment_to_loginFragment)
            }
        }
    }
    private fun signInWithGoogle() {
        viewModel.signInWithGoogle(
            context = requireContext(),
            serverClientId = getString(R.string.default_web_client_id),
            onSuccess = {
                Toast.makeText(
                    requireContext(),
                    "Login successful",
                    Toast.LENGTH_SHORT
                ).show()
                (requireActivity() as MainActivity).showMainApp()
            },
            onFailure = {
                Toast.makeText(
                    requireContext(),
                    it.message ?: "Google sign-in failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}