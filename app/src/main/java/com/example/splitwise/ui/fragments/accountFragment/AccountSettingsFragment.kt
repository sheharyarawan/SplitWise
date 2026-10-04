package com.example.splitwise.ui.fragments.accountFragment
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAccountSettingsBinding
import com.example.splitwise.ui.MainActivity
import com.example.splitwise.ui.viewModels.AccountViewModel
import com.example.splitwise.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class AccountSettingsFragment : Fragment(R.layout.fragment_account_settings) {
    private lateinit var binding: FragmentAccountSettingsBinding
    private val viewModel: AccountViewModel by viewModels()
    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).hideAddExpenseButton()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountSettingsBinding.bind(view)
        setUpClicks()
        observeUser()
    }
    private fun observeUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { state ->
                    binding.apply {
                        when (state) {
                            is UiState.Loading -> {
                            }
                            is UiState.Success -> {
                                accountSettingName.text = state.data.name
                                accountSettingEmail.text = state.data.email
                                accountSettingPassword.text = "••••••••"
                            }
                            is UiState.Error -> {
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
        }
    }
    private fun setUpClicks() {
        binding.apply {
            accountSettingsToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            setUpNameEdit()
            setUpEmailEdit()
            setUpPasswordEdit()
            accountSettingsSaveChangesBtn.setOnClickListener {
                saveChanges()
            }
        }
    }
    private fun setUpNameEdit() {
        binding.accountSettingNameEdit.setOnClickListener {
            binding.apply {
                accountSettingNameEt.setText(accountSettingName.text.toString())
                accountSettingName.isVisible = false
                accountSettingNameEdit.isVisible = false
                accountSettingNameEt.isVisible = true
            }
        }
    }
    private fun setUpEmailEdit() {
        binding.accountSettingEmailEdit.setOnClickListener {
            binding.apply {
                accountSettingEmail.isVisible = false
                accountSettingEmailEdit.isVisible = false
                accountSettingEmailLL.isVisible = true
            }
        }
    }
    private fun setUpPasswordEdit() {
        binding.accountSettingPasswordEdit.setOnClickListener {
            binding.apply {
                accountSettingPassword.isVisible = false
                accountSettingPasswordEdit.isVisible = false
                accountSettingPasswordLL.isVisible = true
            }
        }
    }
    private fun saveChanges() {
        binding.apply {
            val nameChanged = accountSettingNameEt.isVisible
            val emailChanged = accountSettingEmailLL.isVisible
            val passwordChanged = accountSettingPasswordLL.isVisible
            if (!nameChanged && !emailChanged && !passwordChanged) {
                Toast.makeText(
                    requireContext(),
                    "No changes to save",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (nameChanged) {
                saveName(emailChanged, passwordChanged)
                return
            }
            saveEmailAndPassword(emailChanged, passwordChanged)
        }
    }
    private fun saveName(
        emailChanged: Boolean,
        passwordChanged: Boolean
    ) {
        binding.apply {
            val name = accountSettingNameEt.text.toString()
            viewModel.updateName(
                name = name,
                onSuccess = {
                    accountSettingName.text = name.trim()
                    accountSettingName.isVisible = true
                    accountSettingNameEdit.isVisible = true
                    accountSettingNameEt.isVisible = false
                    saveEmailAndPassword(emailChanged, passwordChanged)
                },
                onFailure = { field, message ->
                    handleError(field, message)
                }
            )
        }
    }
    private fun saveEmailAndPassword(
        emailChanged: Boolean,
        passwordChanged: Boolean
    ) {
        binding.apply {
            if (emailChanged) {
                val email = accountSettingNewEmailAddress.text.toString()
                val currentPassword = accountSettingEmailCurrentPassword.text.toString()
                viewModel.updateEmail(
                    newEmail = email,
                    currentPassword = currentPassword,
                    onSuccess = {
                        accountSettingEmail.text = email.trim()
                        accountSettingEmail.isVisible = true
                        accountSettingEmailEdit.isVisible = true
                        accountSettingEmailLL.isVisible = false
                        savePasswordIfNeeded(passwordChanged)
                    },
                    onFailure = { field, message ->
                        handleError(field, message)
                    }
                )
                return
            }
            savePasswordIfNeeded(passwordChanged)
        }
    }
    private fun savePasswordIfNeeded(passwordChanged: Boolean) {
        binding.apply {
            if (!passwordChanged) {
                Toast.makeText(
                    requireContext(),
                    "Changes saved",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            val currentPassword = accountSettingCurrentPassword.text.toString()
            val newPassword = accountSettingsNewPassword.text.toString()
            viewModel.updatePassword(
                currentPassword = currentPassword,
                newPassword = newPassword,
                onSuccess = {
                    accountSettingPassword.isVisible = true
                    accountSettingPasswordEdit.isVisible = true
                    accountSettingPasswordLL.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        "Changes saved",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onFailure = { field, message ->
                    handleError(field, message)
                }
            )
        }
    }
    private fun handleError(
        field: AccountViewModel.AccountField,
        message: String
    ) {
        binding.apply {
            when (field) {
                AccountViewModel.AccountField.NAME -> {
                    accountSettingNameEt.error = message
                }
                AccountViewModel.AccountField.EMAIL -> {
                    accountSettingNewEmailAddress.error = message
                }
                AccountViewModel.AccountField.EMAIL_CURRENT_PASSWORD -> {
                    accountSettingEmailCurrentPassword.error = message
                }
                AccountViewModel.AccountField.CURRENT_PASSWORD -> {
                    accountSettingCurrentPassword.error = message
                }
                AccountViewModel.AccountField.NEW_PASSWORD -> {
                    accountSettingsNewPassword.error = message
                }
                AccountViewModel.AccountField.GENERAL -> {
                    Toast.makeText(
                        requireContext(),
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}