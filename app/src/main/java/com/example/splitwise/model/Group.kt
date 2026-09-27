package com.example.splitwise.model

import com.google.firebase.Timestamp

data class Group(
    val id: String = "",
    val name: String = "",
    val type: GroupType = GroupType.OTHER,
    val createdBy: String = "",
    val memberIds: List<String> = emptyList(),
    val createdAt: Timestamp = Timestamp.now()
)