package com.example.splitwise.viewModels

import com.example.splitwise.repositories.GroupRepository

class GroupViewModel {
    private val repository = GroupRepository()

    fun createGroup(
        name: String,
        type:String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        repository.createGroup(name = name,
            type= type,
            onSuccess = { groupId->
                onSuccess(groupId)
            },
            onFailure={exception ->
                onFailure(exception)
            }
        )
    }
}