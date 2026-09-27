package com.example.splitwise.viewModels

import androidx.lifecycle.ViewModel
import com.example.splitwise.repositories.FriendRepository


class FriendViewModel: ViewModel() {
    val repository= FriendRepository()

        fun addUserToGroup(
            groupId: String,
            email: String,
            name:String,
            onSuccess: () -> Unit,
            onFailure: (Exception) -> Unit
        ) {

            repository.addUserToGroup(
                groupId = groupId,
                email = email,
                name = name,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
}