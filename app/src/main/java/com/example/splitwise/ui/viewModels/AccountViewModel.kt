package com.example.splitwise.ui.viewModels
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
}