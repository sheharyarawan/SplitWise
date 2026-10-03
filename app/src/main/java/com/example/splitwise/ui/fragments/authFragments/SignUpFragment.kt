package com.example.splitwise.ui.fragments.authFragments
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentSignUpBinding
import com.example.splitwise.ui.MainActivity
import com.example.splitwise.ui.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    lateinit var binding: FragmentSignUpBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSignUpBinding.bind(view)
        setUpClicks()
    }
    private fun setUpClicks() {
        binding.apply {
            nameEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    headerLayout.isVisible = false
                    formLayout.isVisible = true
                }
            })
            signUpToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            doneButton.setOnClickListener {
                signUp()
            }
        }
    }
    private fun signUp() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        viewModel.signUpWithEmail(
            name = name,
            email = email,
            password = password,
            onSuccess = {
                (requireActivity() as MainActivity).showMainApp()
            },
            onFailure = { field, message ->
                when (field) {
                    AuthViewModel.SignUpField.NAME -> binding.nameInputLayout.error = message
                    AuthViewModel.SignUpField.EMAIL -> binding.emailInputLayout.error = message
                    AuthViewModel.SignUpField.PASSWORD -> binding.passwordInputLayout.error = message
                    AuthViewModel.SignUpField.GENERAL -> binding.passwordInputLayout.error = message
                }
            }
        )
    }
}