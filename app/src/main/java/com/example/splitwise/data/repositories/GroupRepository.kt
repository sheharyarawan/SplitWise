package com.example.splitwise.data.repositories

import com.example.splitwise.data.model.Group
import com.example.splitwise.data.model.GroupBalance
import com.example.splitwise.data.model.GroupWithBalance
import com.example.splitwise.data.model.User

interface GroupRepository {
    fun createGroup(
        name: String,
        type: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun getGroups(
        onSuccess: (List<Group>) -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun getGroupsWithBalances(
        onSuccess: (List<GroupWithBalance>) -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun getGroupById(
        groupId: String,
        onSuccess: (Group?) -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun getGroupMembers(
        groupId: String,
        onSuccess: (List<User>) -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun getGroupBalances(
        groupId: String,
        onSuccess: (List<GroupBalance>) -> Unit,
        onFailure: (Exception) -> Unit
    )
}