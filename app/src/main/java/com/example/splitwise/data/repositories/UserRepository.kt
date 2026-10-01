package com.example.splitwise.data.repositories

import com.example.splitwise.data.model.User

interface UserRepository {

    fun createUser(
        uid: String,
        user: User,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )
}