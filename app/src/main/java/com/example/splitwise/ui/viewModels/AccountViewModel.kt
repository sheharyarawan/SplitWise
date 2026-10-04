package com.example.splitwise.ui.viewModels
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.splitwise.data.model.User
import com.example.splitwise.data.repositories.UserRepository
import com.example.splitwise.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    enum class AccountField {
        NAME,
        EMAIL,
        EMAIL_CURRENT_PASSWORD,
        CURRENT_PASSWORD,
        NEW_PASSWORD,
        GENERAL
    }
    private val _user = MutableStateFlow<UiState<User>>(UiState.Loading)
    val user: StateFlow<UiState<User>> = _user.asStateFlow()
    fun getCurrentUser() {
        _user.value = UiState.Loading
        repository.getCurrentUser(
            onSuccess = { user ->
                _user.value = UiState.Success(user)
            },
            onFailure = { exception ->
                _user.value = UiState.Error(
                    exception.message ?: "Failed to load user"
                )
            }
        )
    }
    init {
        getCurrentUser()
    }
    fun updateName(
        name: String,
        onSuccess: () -> Unit,
        onFailure: (AccountField, String) -> Unit
    ) {
        val cleanName = name.trim()
        if (cleanName.isBlank()) {
            onFailure(
                AccountField.NAME,
                "Please enter your name"
            )
            return
        }
        repository.updateName(
            name = cleanName,
            onSuccess = onSuccess,
            onFailure = { exception ->
                onFailure(
                    AccountField.GENERAL,
                    exception.message ?: "Failed to update name"
                )
            }
        )
    }
    fun updateEmail(
        newEmail: String,
        currentPassword: String,
        onSuccess: () -> Unit,
        onFailure: (AccountField, String) -> Unit
    ) {
        val cleanEmail = newEmail.trim().lowercase()
        if (!Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            onFailure(
                AccountField.EMAIL,
                "Please enter a valid email address"
            )
            return
        }
        if (currentPassword.isBlank()) {
            onFailure(
                AccountField.EMAIL_CURRENT_PASSWORD,
                "Please enter your current password"
            )
            return
        }
        repository.updateEmail(
            newEmail = cleanEmail,
            currentPassword = currentPassword,
            onSuccess = onSuccess,
            onFailure = { exception ->
                onFailure(
                    AccountField.GENERAL,
                    exception.message ?: "Failed to update email"
                )
            }
        )
    }
    fun updatePassword(
        currentPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onFailure: (AccountField, String) -> Unit
    ) {
        if (currentPassword.isBlank()) {
            onFailure(
                AccountField.CURRENT_PASSWORD,
                "Please enter your current password"
            )
            return
        }
        if (newPassword.length < 8) {
            onFailure(
                AccountField.NEW_PASSWORD,
                "Password must be at least 8 characters"
            )
            return
        }
        if (!newPassword.any { it.isDigit() }) {
            onFailure(
                AccountField.NEW_PASSWORD,
                "Password must contain at least one number"
            )
            return
        }
        if (!newPassword.any { !it.isLetterOrDigit() }) {
            onFailure(
                AccountField.NEW_PASSWORD,
                "Password must contain at least one special character"
            )
            return
        }
        repository.updatePassword(
            currentPassword = currentPassword,
            newPassword = newPassword,
            onSuccess = onSuccess,
            onFailure = { exception ->
                onFailure(
                    AccountField.GENERAL,
                    exception.message ?: "Failed to update password"
                )
            }
        )
    }
}