package com.example.splitwise.data.repositories

import com.example.splitwise.data.model.User

interface FriendRepository {

    fun addFriend(
        name: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun addUserToGroup(
        groupId: String?,
        name: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun getFriends(
        onSuccess: (List<User>) -> Unit,
        onFailure: (Exception) -> Unit
    )
}