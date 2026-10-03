package com.example.splitwise.ui.fragments.accountFragment
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.splitwise.R
import com.example.splitwise.databinding.FragmentAccountBinding
import com.example.splitwise.ui.MainActivity
import com.example.splitwise.ui.viewModels.AccountViewModel
import com.example.splitwise.ui.viewModels.AuthViewModel
import com.example.splitwise.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {
    lateinit var binding: FragmentAccountBinding
    private val viewModel: AccountViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).hideAddExpenseButton()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountBinding.bind(view)
        setUpClicks()
        observeState()
    }
    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                        }
                        is UiState.Success -> {
                            binding.accountName.text = state.data.name
                            binding.accountMail.text = state.data.email
                        }
                        is UiState.Error -> {
                        }
                    }
                }
            }
        }
    }
    private fun setUpClicks() {
        binding.accountEdit.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_accountSettingsFragment)
        }
        binding.accountLogout.setOnClickListener {
            authViewModel.signOut(
                onSuccess = {
                    val activity = requireActivity() as MainActivity
                    activity.hideBottomNav()
                    activity.hideAddExpenseButton()
                    findNavController().setGraph(R.navigation.auth_nav_graph)
                },
                onFailure = { message ->
                    binding.accountLogout.error = message
                }
            )
        }
    }
}