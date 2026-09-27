package com.example.splitwise.repository

import com.example.splitwise.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val firestore= FirebaseFirestore.getInstance()


    fun createUser(
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