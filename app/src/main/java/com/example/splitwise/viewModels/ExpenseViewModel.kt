package com.example.splitwise.viewModels

import androidx.lifecycle.ViewModel
import com.example.splitwise.data.model.Expense
import com.example.splitwise.repositories.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpenseViewModel: ViewModel() {
    private val repository = ExpenseRepository()
    private val _groupExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val groupExpenses: StateFlow<List<Expense>> = _groupExpenses.asStateFlow()

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

        repository.getGroupExpenses(
            groupId = groupId,
            onSuccess = { expenses ->
                _groupExpenses.value = expenses
            },
            onFailure = { exception ->
                onFailure(exception)
            }
        )
    }
}
