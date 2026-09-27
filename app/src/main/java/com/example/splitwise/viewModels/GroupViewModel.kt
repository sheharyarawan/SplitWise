package com.example.splitwise.viewModels

import androidx.lifecycle.ViewModel
import com.example.splitwise.model.Group
import com.example.splitwise.repositories.GroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class GroupViewModel: ViewModel(){
    private val repository = GroupRepository()
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()
    private val _group = MutableStateFlow<Group?>(null)
    val group: StateFlow<Group?> = _group.asStateFlow()

    init {
        getGroups()
    }
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

    fun getGroups( ){
        repository.getGroups(
            onSuccess = { groups->
                _groups.value= groups
            },
            onFailure={ exception ->
            }
        )
    }

    fun getGroupById(id: String){
        repository.getGroupById(id,
            onSuccess = {group->
                _group.value= group
            },
            onFailure = {exception ->
            }
        )
    }
}