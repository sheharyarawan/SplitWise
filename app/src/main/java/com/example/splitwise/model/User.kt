package com.example.splitwise.model

import com.google.firebase.Timestamp

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String? = null,
    val createdAt: Timestamp = Timestamp.now()
)