package com.example.splitwise.model

import com.google.firebase.Timestamp

data class Settlement(
    val id: String = "",
    val groupId: String = "",
    val fromUserId: String = "",
    val fromUserName: String = "",
    val toUserId: String = "",
    val toUserName: String = "",
    val amount: Double = 0.0,
    val createdAt: Timestamp = Timestamp.now()
)