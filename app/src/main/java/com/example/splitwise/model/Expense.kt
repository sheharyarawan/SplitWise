package com.example.splitwise.model

import com.google.firebase.Timestamp

data class Expense(
    val id: String = "",
    val groupId: String? = null,
    val description: String = "",
    val amount: Double = 0.0,
    val paidBy: String = "",
    val splitType: SplitType = SplitType.EQUAL,
    val splits: List<Split> = emptyList(),
    val involvedUserIds: List<String> = emptyList(),
    val date: Timestamp = Timestamp.now(),
    val createdBy: String = "",
    val createdAt: Timestamp = Timestamp.now()
)