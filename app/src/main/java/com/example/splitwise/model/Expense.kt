package com.example.splitwise.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense(
    val id: String = "",
    val groupId: String? = null,
    val description: String = "",
    val amount: Double = 0.0,
    val paidBy: String? = "",
    val splitType: String = "",
    val paidByName: String = "",
    val splits: List<Split> = emptyList(),
    val involvedUserIds: List<String> = emptyList(),
    val date: Timestamp = Timestamp.now(),
    val createdBy: String = "",
    val createdAt: Timestamp = Timestamp.now()
): Parcelable