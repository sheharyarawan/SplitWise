package com.example.splitwise.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise.model.User
import com.example.splitwise.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _authState =
        MutableStateFlow<AuthState>(AuthState.Idle)

    val authState: StateFlow<AuthState> =
        _authState.asStateFlow()

    fun signInWithGoogle(idToken: String) {

        viewModelScope.launch {

            _authState.value = AuthState.Loading

            val result = repository.signInWithGoogle(idToken)

            _authState.value = result.fold(
                onSuccess = { user ->
                    AuthState.Success(user)
                },
                onFailure = { exception ->
                    AuthState.Error(
                        exception.message ?: "Authentication failed"
                    )
                }
            )
        }
    }

    fun signOut(){
        repository.signOut()
    }
}

sealed class AuthState {

    data object Idle : AuthState()

    data object Loading : AuthState()

    data class Success(
        val user: User
    ) : AuthState()

    data class Error(
        val message: String
    ) : AuthState()
}