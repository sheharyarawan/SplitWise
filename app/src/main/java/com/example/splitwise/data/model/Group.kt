package com.example.splitwise.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val createdBy: String = "",
    val memberIds: List<String> = emptyList(),
    val createdAt: Timestamp = Timestamp.now()
): Parcelable