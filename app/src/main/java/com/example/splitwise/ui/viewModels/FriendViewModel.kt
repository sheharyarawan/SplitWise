package com.example.splitwise.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.splitwise.data.model.User
import com.example.splitwise.data.repositories.FriendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FriendViewModel : ViewModel() {

    private val repository = FriendRepository()

    // ---------------------------------------------------------
    // FRIENDS LIST
    // ---------------------------------------------------------

    private val _friends = MutableStateFlow<List<User>>(emptyList())
    val friends: StateFlow<List<User>> = _friends.asStateFlow()


    // ---------------------------------------------------------
    // ADD FRIEND
    // ---------------------------------------------------------

    fun addFriend(
        name: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        repository.addFriend(
            name = name,
            email = email,
            onSuccess = {
                onSuccess()
            },
            onFailure = { exception ->
                onFailure(exception)
            }
        )
    }


    // ---------------------------------------------------------
    // ADD USER TO GROUP
    // ---------------------------------------------------------

    fun addUserToGroup(
        groupId: String?,
        name: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        repository.addUserToGroup(
            groupId = groupId,
            name = name,
            email = email,
            onSuccess = {
                onSuccess()
                getFriends()
            },
            onFailure = { exception ->
                onFailure(exception)
            }
        )
    }


    // ---------------------------------------------------------
    // GET FRIENDS
    // ---------------------------------------------------------

    fun getFriends() {

        repository.getFriends(
            onSuccess = { friends ->
                _friends.value = friends
            },
            onFailure = { exception ->
                // We can add an error StateFlow later if needed.
            }
        )
    }


    // ---------------------------------------------------------
    // LOAD FRIENDS WHEN VIEWMODEL IS CREATED
    // ---------------------------------------------------------

    init {
        getFriends()
    }
}