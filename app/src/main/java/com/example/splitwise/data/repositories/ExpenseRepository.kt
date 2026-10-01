package com.example.splitwise.data.repositories

import com.example.splitwise.data.model.Expense

interface ExpenseRepository {
    fun addExpense(
        expense: Expense,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun getGroupExpenses(
        groupId: String,
        onSuccess: (List<Expense>) -> Unit,
        onFailure: (Exception) -> Unit
    )
}