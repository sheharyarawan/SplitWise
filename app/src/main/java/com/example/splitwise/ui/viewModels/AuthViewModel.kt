package com.example.splitwise.ui.viewModels
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.splitwise.data.repositories.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun signInWithGoogle(
        context: Context,
        serverClientId: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        userRepository.signInWithGoogle(
            context = context,
            serverClientId = serverClientId,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}