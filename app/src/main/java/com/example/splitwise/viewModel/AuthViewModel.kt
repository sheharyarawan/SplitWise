package com.example.splitwise.viewModel

import androidx.lifecycle.ViewModel
import com.example.splitwise.model.User
import com.example.splitwise.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val userRepository = UserRepository()

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