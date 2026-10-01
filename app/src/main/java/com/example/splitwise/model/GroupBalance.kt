package com.example.splitwise.model

data class GroupBalance(
    val fromUserId: String = "",
    val fromUserName: String = "",
    val toUserId: String = "",
    val toUserName: String = "",
    val amount: Double = 0.0
)