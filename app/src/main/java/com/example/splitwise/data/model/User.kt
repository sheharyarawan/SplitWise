package com.example.splitwise.data.model
import com.google.firebase.Timestamp
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val uid: String? = null,
    val isRegistered: Boolean = false,
    val createdAt: Timestamp? = null
)