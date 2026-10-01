package com.example.splitwise.repositories

import com.example.splitwise.model.Expense
import com.example.splitwise.model.Group
import com.example.splitwise.model.GroupBalance
import com.example.splitwise.model.GroupWithBalance
import com.example.splitwise.model.Settlement
import com.example.splitwise.model.User
import com.example.splitwise.utils.GroupBalanceCalculator
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class GroupRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun createGroup(
        name: String,
        type: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val currentUser = auth.currentUser

        if (currentUser == null) {
            onFailure(
                Exception("User is not logged in")
            )
            return
        }

        val userId = currentUser.uid

        val groupId = firestore
            .collection("groups")
            .document()
            .id

        val group = Group(
            id = groupId,
            name = name,
            type = type,
            createdBy = userId,
            memberIds = listOf(userId),
            createdAt = Timestamp.now()
        )

        val groupRef = firestore
            .collection("groups")
            .document(groupId)

        val memberRef = groupRef
            .collection("members")
            .document(userId)

        val batch = firestore.batch()

        batch.set(groupRef, group)

        batch.set(
            memberRef,
            mapOf(
                "joinedAt" to Timestamp.now(),
                "role" to "admin"
            )
        )

        batch.commit()
            .addOnSuccessListener {
                onSuccess(groupId)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getGroups(
        onSuccess: (List<Group>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val currentUser = auth.currentUser

        if (currentUser == null) {
            onFailure(
                Exception("User is not logged in")
            )
            return
        }

        firestore
            .collection("groups")
            .whereArrayContains(
                "memberIds",
                currentUser.uid
            )
            .addSnapshotListener { snapshots, error ->

                if (error != null) {
                    onFailure(error)
                    return@addSnapshotListener
                }

                val groups =
                    snapshots
                        ?.documents
                        ?.mapNotNull { document ->

                            document
                                .toObject(Group::class.java)
                                ?.copy(
                                    id = document.id
                                )
                        }
                        ?: emptyList()

                onSuccess(groups)
            }
    }

    // --------------------------------------------------
    // GET GROUPS WITH BALANCES
    // --------------------------------------------------

    fun getGroupsWithBalances(
        onSuccess: (List<GroupWithBalance>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val currentUser = auth.currentUser

        if (currentUser == null) {
            onFailure(
                Exception("User is not logged in")
            )
            return
        }

        firestore
            .collection("groups")
            .whereArrayContains(
                "memberIds",
                currentUser.uid
            )
            .get()
            .addOnSuccessListener { snapshot ->

                val groups =
                    snapshot.documents.mapNotNull { document ->

                        document
                            .toObject(Group::class.java)
                            ?.copy(
                                id = document.id
                            )
                    }

                if (groups.isEmpty()) {
                    onSuccess(emptyList())
                    return@addOnSuccessListener
                }

                getBalancesForGroups(
                    groups = groups,
                    currentUserId = currentUser.uid,
                    onSuccess = onSuccess,
                    onFailure = onFailure
                )
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    private fun getBalancesForGroups(
        groups: List<Group>,
        currentUserId: String,
        onSuccess: (List<GroupWithBalance>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val requests = groups.map { group ->

            val expensesTask =
                firestore
                    .collection("expenses")
                    .whereEqualTo(
                        "groupId",
                        group.id
                    )
                    .get()

            val settlementsTask =
                firestore
                    .collection("settlements")
                    .whereEqualTo(
                        "groupId",
                        group.id
                    )
                    .get()

            Tasks
                .whenAllSuccess<com.google.firebase.firestore.QuerySnapshot>(
                    expensesTask,
                    settlementsTask
                )
                .continueWith { task ->

                    val results = task.result

                    val expenseSnapshot = results[0]
                    val settlementSnapshot = results[1]

                    val expenses =
                        expenseSnapshot.documents.mapNotNull { document ->

                            document
                                .toObject(Expense::class.java)
                                ?.copy(
                                    id = document.id
                                )
                        }

                    val settlements =
                        settlementSnapshot.documents.mapNotNull { document ->

                            document
                                .toObject(Settlement::class.java)
                                ?.copy(
                                    id = document.id
                                )
                        }

                    val balances =
                        GroupBalanceCalculator.calculate(
                            expenses = expenses,
                            settlements = settlements
                        )

                    val youOwe =
                        GroupBalanceCalculator.getYouOwe(
                            balances = balances,
                            currentUserId = currentUserId
                        )

                    val youGetBack =
                        GroupBalanceCalculator.getYouGetBack(
                            balances = balances,
                            currentUserId = currentUserId
                        )

                    GroupWithBalance(
                        group = group,
                        youGetBack = youGetBack,
                        youOwe = youOwe,
                        balances = balances
                    )
                }
        }

        Tasks
            .whenAllSuccess<GroupWithBalance>(requests)
            .addOnSuccessListener { results ->

                onSuccess(results)
            }
            .addOnFailureListener { exception ->

                onFailure(exception)
            }
    }

    fun getGroupById(
        groupId: String,
        onSuccess: (Group?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        firestore
            .collection("groups")
            .document(groupId)
            .get()
            .addOnSuccessListener { document ->

                if (document.exists()) {

                    val group =
                        document
                            .toObject(Group::class.java)
                            ?.copy(
                                id = document.id
                            )

                    onSuccess(group)

                } else {

                    onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->

                onFailure(exception)
            }
    }

    fun getGroupMembers(
        groupId: String,
        onSuccess: (List<User>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        firestore
            .collection("groups")
            .document(groupId)
            .addSnapshotListener { document, exception ->

                if (exception != null) {
                    onFailure(exception)
                    return@addSnapshotListener
                }

                if (document == null || !document.exists()) {

                    onFailure(
                        Exception("Group not found")
                    )

                    return@addSnapshotListener
                }

                val memberIds =
                    document.get("memberIds")
                            as? List<String>
                        ?: emptyList()

                if (memberIds.isEmpty()) {
                    onSuccess(emptyList())
                    return@addSnapshotListener
                }

                val requests =
                    memberIds.map { userId ->

                        firestore
                            .collection("users")
                            .document(userId)
                            .get()
                    }

                Tasks
                    .whenAllSuccess<com.google.firebase.firestore.DocumentSnapshot>(
                        requests
                    )
                    .addOnSuccessListener { documents ->

                        val members =
                            documents.mapNotNull { userDocument ->

                                userDocument
                                    .toObject(User::class.java)
                                    ?.copy(
                                        id = userDocument.id
                                    )
                            }

                        onSuccess(members)
                    }
                    .addOnFailureListener { exception ->

                        onFailure(exception)
                    }
            }
    }

    fun getGroupBalances(
        groupId: String,
        onSuccess: (List<GroupBalance>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val expensesTask = firestore
            .collection("expenses")
            .whereEqualTo("groupId", groupId)
            .get()

        val settlementsTask = firestore
            .collection("settlements")
            .whereEqualTo("groupId", groupId)
            .get()

        Tasks.whenAllSuccess<QuerySnapshot>(
            expensesTask,
            settlementsTask
        )
            .addOnSuccessListener { results ->

                val expenseSnapshot = results[0]
                val settlementSnapshot = results[1]

                val expenses =
                    expenseSnapshot.documents.mapNotNull { document ->

                        document
                            .toObject(Expense::class.java)
                            ?.copy(
                                id = document.id
                            )
                    }

                val settlements =
                    settlementSnapshot.documents.mapNotNull { document ->

                        document
                            .toObject(Settlement::class.java)
                            ?.copy(
                                id = document.id
                            )
                    }

                val balances =
                    GroupBalanceCalculator.calculate(
                        expenses = expenses,
                        settlements = settlements
                    )

                onSuccess(balances)
            }
            .addOnFailureListener { exception ->

                onFailure(exception)
            }
    }


}