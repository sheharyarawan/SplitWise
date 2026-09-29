package com.example.splitwise.model

import com.google.firebase.Timestamp

data class Settlement(
    val id: String = "",
    val groupId: String? = null,
    val fromUser: String = "",
    val toUser: String = "",
    val amount: Double = 0.0,
    val date: Timestamp = Timestamp.now(),
)