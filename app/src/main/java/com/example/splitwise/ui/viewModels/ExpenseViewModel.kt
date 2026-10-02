package com.example.splitwise.ui.viewModels
import androidx.lifecycle.ViewModel
import com.example.splitwise.data.model.Expense
import com.example.splitwise.data.repositories.ExpenseRepository
import com.example.splitwise.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {
    private val _groupExpenses = MutableStateFlow<UiState<List<Expense>>>(UiState.Loading)
    val groupExpenses: StateFlow<UiState<List<Expense>>> = _groupExpenses.asStateFlow()
    fun addExpense(
        expense: Expense,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.addExpense(
            expense = expense,
            onSuccess = {
                onSuccess()
            },
            onFailure = { exception ->
                onFailure(exception)
            }
        )
    }
    fun getGroupExpenses(
        groupId: String,
        onFailure: (Exception) -> Unit
    ) {
        _groupExpenses.value = UiState.Loading
        repository.getGroupExpenses(
            groupId = groupId,
            onSuccess = { expenses ->
                _groupExpenses.value = UiState.Success(expenses)
            },
            onFailure = { exception ->
                _groupExpenses.value = UiState.Error(
                    exception.message ?: "Failed to load group expenses"
                )
                onFailure(exception)
            }
        )
    }
}