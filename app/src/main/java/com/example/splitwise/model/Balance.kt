package com.example.splitwise.model

import com.google.firebase.Timestamp

data class Balance(
    val otherUserId: String = "",
    val amount: Double = 0.0,
    val updatedAt: Timestamp = Timestamp.now()
)