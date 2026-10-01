package com.example.splitwise.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.splitwise.data.model.Group
import com.example.splitwise.data.model.GroupBalance
import com.example.splitwise.data.model.GroupMemberBalance
import com.example.splitwise.data.model.GroupWithBalance
import com.example.splitwise.data.model.User
import com.example.splitwise.data.repositories.GroupRepository
import com.example.splitwise.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val repository: GroupRepository
) : ViewModel() {
    private val _groups =
        MutableStateFlow<UiState<List<Group>>>(UiState.Loading)
    val groups: StateFlow<UiState<List<Group>>> =
        _groups.asStateFlow()
    private val _group =
        MutableStateFlow<UiState<Group>>(UiState.Loading)
    val group: StateFlow<UiState<Group>> =
        _group.asStateFlow()
    private val _groupMembers =
        MutableStateFlow<UiState<List<User>>>(UiState.Loading)
    val groupMembers: StateFlow<UiState<List<User>>> =
        _groupMembers.asStateFlow()
    private val _memberBalances =
        MutableStateFlow<UiState<List<GroupMemberBalance>>>(
            UiState.Loading
        )
    val memberBalances: StateFlow<UiState<List<GroupMemberBalance>>> =
        _memberBalances.asStateFlow()
    private val _groupsWithBalances =
        MutableStateFlow<UiState<List<GroupWithBalance>>>(
            UiState.Loading
        )
    val groupsWithBalances: StateFlow<UiState<List<GroupWithBalance>>> =
        _groupsWithBalances.asStateFlow()
    private val _groupBalances =
        MutableStateFlow<UiState<List<GroupBalance>>>(
            UiState.Loading
        )
    val groupBalances: StateFlow<UiState<List<GroupBalance>>> =
        _groupBalances.asStateFlow()

    init {
        getGroups()
        getGroupsWithBalances()
    }
    fun createGroup(
        name: String,
        type: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.createGroup(
            name = name,
            type = type,
            onSuccess = { groupId ->
                onSuccess(groupId)
            },
            onFailure = { exception ->
                onFailure(exception)
            }
        )
    }
    fun getGroups() {
        _groups.value = UiState.Loading
        repository.getGroups(
            onSuccess = { groups ->
                _groups.value = UiState.Success(groups)
            },
            onFailure = { exception ->
                _groups.value = UiState.Error(
                    exception.message ?: "Failed to load groups"
                )
            }
        )
    }
    fun getGroupById(id: String) {
        _group.value = UiState.Loading
        repository.getGroupById(
            groupId = id,
            onSuccess = { group ->
                if (group != null) {
                    _group.value = UiState.Success(group)
                } else {
                    _group.value = UiState.Error(
                        "Group not found"
                    )
                }
            },
            onFailure = { exception ->
                _group.value = UiState.Error(
                    exception.message ?: "Failed to load group"
                )
            }
        )
    }
    fun getGroupMembers(
        groupId: String
    ) {
        _groupMembers.value = UiState.Loading
        repository.getGroupMembers(
            groupId = groupId,
            onSuccess = { members ->
                _groupMembers.value = UiState.Success(members)
            },
            onFailure = { exception ->
                _groupMembers.value = UiState.Error(
                    exception.message ?: "Failed to load group members"
                )
            }
        )
    }
    fun getGroupsWithBalances() {
        _groupsWithBalances.value = UiState.Loading
        repository.getGroupsWithBalances(
            onSuccess = { groups ->
                _groupsWithBalances.value =
                    UiState.Success(groups)
            },
            onFailure = { exception ->
                _groupsWithBalances.value =
                    UiState.Error(
                        exception.message
                            ?: "Failed to load groups with balances"
                    )
            }
        )
    }
    fun getGroupBalances(
        groupId: String
    ) {
        _groupBalances.value = UiState.Loading
        _memberBalances.value = UiState.Loading
        repository.getGroupBalances(
            groupId = groupId,
            onSuccess = { balances ->
                _groupBalances.value =
                    UiState.Success(balances)
                calculateMemberBalancesFromGroupBalances(
                    balances = balances,
                    members = getCurrentMembers()
                )
            },
            onFailure = { exception ->
                val message =
                    exception.message
                        ?: "Failed to load group balances"
                _groupBalances.value =
                    UiState.Error(message)
                _memberBalances.value =
                    UiState.Error(message)
            }
        )
    }
    private fun getCurrentMembers(): List<User> {
        return when (val state = _groupMembers.value) {
            is UiState.Success -> state.data
            else -> emptyList()
        }
    }
    private fun calculateMemberBalancesFromGroupBalances(
        balances: List<GroupBalance>,
        members: List<User>
    ) {
        val memberBalances = members.map { member ->
            val getBack = balances
                .filter { balance ->
                    balance.toUserId == member.id
                }
                .sumOf { it.amount }
            val owe = balances
                .filter { balance ->
                    balance.fromUserId == member.id
                }
                .sumOf { it.amount }
            GroupMemberBalance(
                userId = member.id,
                name = member.name,
                email = member.email,
                balance = getBack - owe
            )
        }
        _memberBalances.value =
            UiState.Success(memberBalances)
    }
}