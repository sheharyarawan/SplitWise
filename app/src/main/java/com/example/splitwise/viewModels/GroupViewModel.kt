package com.example.splitwise.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.splitwise.model.Expense
import com.example.splitwise.model.Group
import com.example.splitwise.model.GroupMemberBalance
import com.example.splitwise.model.User
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

    private val _groupMembers =
        MutableStateFlow<List<User>>(emptyList())

    val groupMembers: StateFlow<List<User>> =
        _groupMembers.asStateFlow()

    private val _memberBalances =
        MutableStateFlow<List<GroupMemberBalance>>(emptyList())

    val memberBalances: StateFlow<List<GroupMemberBalance>> =
        _memberBalances.asStateFlow()

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

    fun getGroupMembers(
        groupId: String,
        onFailure: (Exception) -> Unit
    ) {

        repository.getGroupMembers(
            groupId = groupId,
            onSuccess = { members ->
                _groupMembers.value = members
            },
            onFailure = { exception ->

                onFailure(exception)
            }
        )
    }

    fun calculateMemberBalances(
        members: List<User>,
        expenses: List<Expense>
    ) {
        val balances = members.map { member ->

            var totalPaid = 0.0
            var totalOwed = 0.0

            expenses.forEach { expense ->

                // Amount this member paid
                if (expense.paidBy == member.id) {
                    totalPaid += expense.amount
                }

                // Amount this member owes
                val memberSplit = expense.splits.find {
                    it.userId == member.id
                }

                if (memberSplit != null) {
                    totalOwed += memberSplit.amount
                }
            }

            GroupMemberBalance(
                userId = member.id,
                name = member.name,
                email = member.email,
                balance = totalPaid - totalOwed
            )
        }

        _memberBalances.value = balances
    }
}