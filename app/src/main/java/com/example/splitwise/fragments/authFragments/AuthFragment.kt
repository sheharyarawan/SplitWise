package com.example.splitwise.fragments.authFragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAuthBinding
import com.example.splitwise.viewModel.AuthState
import com.example.splitwise.viewModel.AuthViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    private lateinit var credentialManager: CredentialManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAuthBinding.bind(view)

        credentialManager = CredentialManager.create(requireContext())

        binding.signInGoogleButton.setOnClickListener {
            signInWithGoogle()
        }

        observeAuthState()
    }

    private fun signInWithGoogle() {

        viewLifecycleOwner.lifecycleScope.launch {

            try {

                binding.signInGoogleButton.isEnabled = false

                val googleOption = GetGoogleIdOption.Builder()
                    .setServerClientId(
                        getString(R.string.default_web_client_id)
                    )
                    .setFilterByAuthorizedAccounts(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleOption)
                    .build()

                val result = credentialManager.getCredential(
                    requireContext(),
                    request
                )

                val credential = result.credential

                if (
                    credential.type ==
                    GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {

                    val googleCredential =
                        GoogleIdTokenCredential.createFrom(
                            credential.data
                        )

                    viewModel.signInWithGoogle(
                        googleCredential.idToken
                    )
                }

            } catch (e: Exception) {

                binding.signInGoogleButton.isEnabled = true

                Toast.makeText(
                    requireContext(),
                    e.message ?: "Google sign in failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeAuthState() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.authState.collect { state ->

                when (state) {

                    AuthState.Idle -> {
                        binding.signInGoogleButton.isEnabled = true
                    }

                    AuthState.Loading -> {
                        binding.signInGoogleButton.isEnabled = false
                    }

                    is AuthState.Success -> {

                        binding.signInGoogleButton.isEnabled = true

                        Toast.makeText(
                            requireContext(),
                            "Welcome ${state.user.name}",
                            Toast.LENGTH_SHORT
                        ).show()

                        requireActivity().recreate()
                    }

                    is AuthState.Error -> {

                        binding.signInGoogleButton.isEnabled = true

                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}