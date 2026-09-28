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

    fun getGroupExpenses(
        groupId: String,
        onSuccess: (List<Expense>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        firestore
            .collection("expenses")
            .whereEqualTo("groupId", groupId)
            .addSnapshotListener { snapshot, exception ->

                if (exception != null) {
                    onFailure(exception)
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    onFailure(
                        Exception("Unable to load expenses")
                    )
                    return@addSnapshotListener
                }

                val expenses =
                    snapshot.documents.mapNotNull { document ->

                        document
                            .toObject(Expense::class.java)
                            ?.copy(
                                id = document.id
                            )
                    }

                onSuccess(expenses)
            }
    }
}