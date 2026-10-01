package com.example.splitwise.ui.viewModels
import androidx.lifecycle.ViewModel
import com.example.splitwise.data.model.User
import com.example.splitwise.data.repositories.FriendRepository
import com.example.splitwise.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val repository: FriendRepository
) : ViewModel() {
    private val _friends = MutableStateFlow<UiState<List<User>>>(UiState.Loading)
    val friends: StateFlow<UiState<List<User>>> = _friends.asStateFlow()
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
            },
            onFailure = { exception ->
                onFailure(exception)
            }
        )
    }
    fun getFriends() {
        _friends.value = UiState.Loading
        repository.getFriends(
            onSuccess = { friends ->
                _friends.value = UiState.Success(friends)
            },
            onFailure = { exception ->
                _friends.value = UiState.Error(
                    exception.message ?: "Failed to load friends"
                )
            }
        )
    }
    init {
        getFriends()
    }
}