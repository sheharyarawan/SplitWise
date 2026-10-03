package com.example.splitwise.data.model
import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val uid: String? = null,
    val isRegistered: Boolean = false,
    val friends: List<String> = emptyList(),
    val createdAt: Timestamp? = null
): Parcelable