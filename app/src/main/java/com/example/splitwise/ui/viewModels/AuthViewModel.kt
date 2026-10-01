package com.example.splitwise.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.splitwise.data.model.User
import com.example.splitwise.data.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel(){
    fun saveUser(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val firebaseUser = auth.currentUser ?: return

        val user = User(
            name = firebaseUser.displayName ?: "",
            email = firebaseUser.email ?: ""
        )

        userRepository.createUser(
            uid = firebaseUser.uid,
            user = user,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}