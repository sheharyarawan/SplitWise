package com.example.splitwise.fragments.authFragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAuthBinding
import com.example.splitwise.viewModel.AuthViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private lateinit var binding: FragmentAuthBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager

    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAuthBinding.bind(view)

        auth = FirebaseAuth.getInstance()

        credentialManager = CredentialManager.create(
            requireContext()
        )

        handleButtonClicks()
    }

    private fun handleButtonClicks() {

        binding.signInGoogleButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(
                getString(R.string.default_web_client_id)
            )
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewLifecycleOwner.lifecycleScope.launch {

            try {

                val result = credentialManager.getCredential(
                    requireContext(),
                    request
                )

                handleCredential(
                    result.credential
                )

            } catch (e: androidx.credentials.exceptions.NoCredentialException) {

                Log.e(
                    "GoogleSignIn",
                    "No Google credentials available",
                    e
                )

                Toast.makeText(
                    requireContext(),
                    "No Google account available on this device",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: Exception) {

                Log.e(
                    "GoogleSignIn",
                    "Google sign-in failed",
                    e
                )

                Toast.makeText(
                    requireContext(),
                    "Google sign-in failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleCredential(
        credential: androidx.credentials.Credential
    ) {

        if (
            credential is CustomCredential &&
            credential.type ==
            GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {

            try {

                val googleCredential =
                    GoogleIdTokenCredential.createFrom(
                        credential.data
                    )

                firebaseAuthWithGoogle(
                    googleCredential.idToken
                )

            } catch (e: GoogleIdTokenParsingException) {

                Log.e(
                    "GoogleSignIn",
                    "Invalid Google ID token",
                    e
                )

                Toast.makeText(
                    requireContext(),
                    "Invalid Google credential",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } else {

            Log.e(
                "GoogleSignIn",
                "Unexpected credential type"
            )

            Toast.makeText(
                requireContext(),
                "Unexpected Google credential",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun firebaseAuthWithGoogle(
        idToken: String
    ) {

        val credential = GoogleAuthProvider.getCredential(
            idToken,
            null
        )

        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->

                if (task.isSuccessful) {

                    val user = auth.currentUser

                    Log.d(
                        "GoogleSignIn",
                        "Login successful: ${user?.uid}"
                    )

                    viewModel.saveUser(

                        onSuccess = {

                            Toast.makeText(
                                requireContext(),
                                "Login successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            val activity =
                                requireActivity() as MainActivity

                            activity.showMainApp()
                        },

                        onFailure = { exception ->

                            Log.e(
                                "Firestore",
                                "Failed to save user",
                                exception
                            )

                            Toast.makeText(
                                requireContext(),
                                "Failed to save user",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )

                } else {

                    Log.e(
                        "GoogleSignIn",
                        "Firebase authentication failed",
                        task.exception
                    )

                    Toast.makeText(
                        requireContext(),
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}