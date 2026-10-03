package com.example.splitwise.ui.viewModels
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.splitwise.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun signInWithGoogle(
        context: Context,
        serverClientId: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        userRepository.signInWithGoogle(
            context = context,
            serverClientId = serverClientId,
            onSuccess = { firebaseUser ->
                userRepository.getOrCreateUser(
                    firebaseUser = firebaseUser,
                    onSuccess = { userId ->
                        onSuccess(userId)
                    },
                    onFailure = onFailure
                )
            },
            onFailure = onFailure
        )
    }
}