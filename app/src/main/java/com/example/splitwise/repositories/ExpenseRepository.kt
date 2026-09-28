package com.example.splitwise.repositories

import com.example.splitwise.model.Expense
import com.google.firebase.firestore.FirebaseFirestore

class ExpenseRepository {
    val firestore= FirebaseFirestore.getInstance()

    fun addExpense(
        expense: Expense,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit){

        val expenseReference= firestore.collection("expenses")
            .document()

        val expenseWithId =
            expense.copy(
                id = expenseReference.id
            )
        expenseReference
            .set(expenseWithId)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}