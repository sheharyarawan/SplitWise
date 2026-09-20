package com.example.splitwise.fragments.accountFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.splitwise.MainActivity
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAccountSettingsBinding

class AccountSettingsFragment : Fragment(R.layout.fragment_account_settings) {

    private lateinit var binding: FragmentAccountSettingsBinding

    override fun onResume() {
        super.onResume()

        (requireActivity() as MainActivity).hideAddExpenseButton()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAccountSettingsBinding.bind(view)

        setUpNameEdit()
        setUpEmailEdit()
        setUpPasswordEdit()
        binding.accountSettingsToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.accountSettingsSaveChangesBtn.setOnClickListener {

        }
    }

    private fun setUpNameEdit() {
        binding.accountSettingNameEdit.setOnClickListener {

            binding.accountSettingNameEt.setText(
                binding.accountSettingName.text.toString()
            )

            binding.accountSettingName.visibility = View.GONE
            binding.accountSettingNameEdit.visibility = View.GONE
            binding.accountSettingNameEt.visibility = View.VISIBLE
        }
    }

    private fun setUpEmailEdit() {
        binding.accountSettingEmailEdit.setOnClickListener {

            binding.accountSettingEmail.visibility = View.GONE
            binding.accountSettingEmailEdit.visibility = View.GONE
            binding.accountSettingEmailLL.visibility = View.VISIBLE
        }
    }

    private fun setUpPasswordEdit() {
        binding.accountSettingPasswordEdit.setOnClickListener {

            binding.accountSettingPassword.visibility = View.GONE
            binding.accountSettingPasswordEdit.visibility = View.GONE
            binding.accountSettingPasswordLL.visibility = View.VISIBLE
        }
    }
}