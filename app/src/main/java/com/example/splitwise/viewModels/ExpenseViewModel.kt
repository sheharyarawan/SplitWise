package com.example.splitwise.viewModels

import androidx.lifecycle.ViewModel
import com.example.splitwise.model.Expense
import com.example.splitwise.repositories.ExpenseRepository

class ExpenseViewModel: ViewModel() {

    private val repository =
        ExpenseRepository()

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
}
