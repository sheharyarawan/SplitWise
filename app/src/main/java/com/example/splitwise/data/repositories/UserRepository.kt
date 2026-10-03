package com.example.splitwise.data.repositories

import android.content.Context
import com.example.splitwise.data.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ListenerRegistration

interface UserRepository {
    fun signInWithGoogle(
        context: Context,
        serverClientId: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun signUpWithEmail(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun signInWithEmail(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun getOrCreateUser(
        firebaseUser: FirebaseUser,
        name: String?= null,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun getCurrentUser(
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ): ListenerRegistration?
    fun signOut(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )
}