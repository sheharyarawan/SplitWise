package com.example.splitwise.data.repositories

import android.content.Context
import com.google.firebase.auth.FirebaseUser
interface UserRepository {
    fun signInWithGoogle(
        context: Context,
        serverClientId: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun getOrCreateUser(
        firebaseUser: FirebaseUser,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )
}