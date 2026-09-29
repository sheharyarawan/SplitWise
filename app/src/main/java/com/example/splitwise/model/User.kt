package com.example.splitwise.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val friends: List<String> = emptyList(),
    val phoneNumber: String? = null,
    val createdAt: Timestamp = Timestamp.now()
): Parcelable