package com.example.splitwise.data.repositories

import com.example.splitwise.data.model.User
import com.google.firebase.firestore.FirebaseFirestore

class UserRepositoryImpl: UserRepository {
    private val firestore= FirebaseFirestore.getInstance()

    override fun createUser(
        uid: String,
        user: User,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore
            .collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}