package com.example.splitwise.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.splitwise.data.model.Group
import com.example.splitwise.data.model.GroupBalance
import com.example.splitwise.data.model.GroupMemberBalance
import com.example.splitwise.data.model.GroupWithBalance
import com.example.splitwise.data.model.User
import com.example.splitwise.data.repositories.GroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GroupViewModel : ViewModel() {

    private val repository = GroupRepository()

    private val _groups =
        MutableStateFlow<List<Group>>(emptyList())

    val groups: StateFlow<List<Group>> =
        _groups.asStateFlow()


    private val _group =
        MutableStateFlow<Group?>(null)

    val group: StateFlow<Group?> =
        _group.asStateFlow()


    private val _groupMembers =
        MutableStateFlow<List<User>>(emptyList())

    val groupMembers: StateFlow<List<User>> =
        _groupMembers.asStateFlow()


    /*
     * These are the balances shown in GroupSettingsFragment.
     *
     * They are now generated from groupBalances,
     * which already includes expenses + settlements.
     */
    private val _memberBalances =
        MutableStateFlow<List<GroupMemberBalance>>(emptyList())

    val memberBalances: StateFlow<List<GroupMemberBalance>> =
        _memberBalances.asStateFlow()


    /*
     * Used by GroupsFragment.
     *
     * Contains:
     * - group
     * - you owe
     * - you get back
     * - all pair-wise balances
     */
    private val _groupsWithBalances =
        MutableStateFlow<List<GroupWithBalance>>(emptyList())

    val groupsWithBalances: StateFlow<List<GroupWithBalance>> =
        _groupsWithBalances.asStateFlow()


    /*
     * Contains the final pair-wise balances for one group.
     *
     * Example:
     *
     * You -> Ali -> Rs 300
     * Ahmed -> You -> Rs 200
     */
    private val _groupBalances =
        MutableStateFlow<List<GroupBalance>>(emptyList())

    val groupBalances: StateFlow<List<GroupBalance>> =
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

        repository.getGroups(

            onSuccess = { groups ->
                _groups.value = groups
            },

            onFailure = { exception ->
                Log.e(
                    "GroupViewModel",
                    "Failed to get groups",
                    exception
                )
            }
        )
    }


    fun getGroupById(id: String) {

        repository.getGroupById(
            id,

            onSuccess = { group ->
                _group.value = group
            },

            onFailure = { exception ->
                Log.e(
                    "GroupViewModel",
                    "Failed to get group",
                    exception
                )
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


    fun getGroupsWithBalances() {

        repository.getGroupsWithBalances(

            onSuccess = { groups ->
                _groupsWithBalances.value = groups
            },

            onFailure = { exception ->

                Log.e(
                    "GroupViewModel",
                    "Failed to get groups with balances",
                    exception
                )
            }
        )
    }


    /*
     * Get all final balances for one group.
     *
     * Repository gets:
     *
     * expenses
     * +
     * settlements
     *
     * and GroupBalanceCalculator calculates
     * who owes whom.
     */
    fun getGroupBalances(
        groupId: String,
        onFailure: (Exception) -> Unit
    ) {

        repository.getGroupBalances(
            groupId = groupId,

            onSuccess = { balances ->

                _groupBalances.value = balances

                /*
                 * Also update the member balances used
                 * by GroupSettingsFragment.
                 */
                calculateMemberBalancesFromGroupBalances(
                    balances = balances,
                    members = _groupMembers.value
                )
            },

            onFailure = { exception ->

                Log.e(
                    "GroupViewModel",
                    "Failed to get group balances",
                    exception
                )

                onFailure(exception)
            }
        )
    }


    /*
     * Converts pair-wise GroupBalance data into
     * GroupMemberBalance data for the existing
     * GroupMemberAdapter.
     *
     * Example:
     *
     * You owe Ali Rs 300
     * Ahmed owes you Rs 500
     *
     * Ali     -> -300
     * Ahmed   -> +500
     */
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

        _memberBalances.value = memberBalances
    }
}